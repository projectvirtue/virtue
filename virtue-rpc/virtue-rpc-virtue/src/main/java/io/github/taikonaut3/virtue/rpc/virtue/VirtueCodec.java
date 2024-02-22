package io.github.taikonaut3.virtue.rpc.virtue;

import io.github.taikonaut3.virtue.common.constant.Components;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.constant.Mode;
import io.github.taikonaut3.virtue.common.constant.ModeContainer;
import io.github.taikonaut3.virtue.common.exception.CodecException;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.rpc.virtue.envelope.VirtueEnvelope;
import io.github.taikonaut3.virtue.rpc.virtue.header.Header;
import io.github.taikonaut3.virtue.rpc.virtue.header.VirtueHeader;
import io.github.taikonaut3.virtue.serialization.Serializer;
import io.github.taikonaut3.virtue.transport.Request;
import io.github.taikonaut3.virtue.transport.Response;
import io.github.taikonaut3.virtue.transport.byteutils.ByteReader;
import io.github.taikonaut3.virtue.transport.byteutils.ByteWriter;
import io.github.taikonaut3.virtue.transport.codec.Codec;
import io.github.taikonaut3.virtue.transport.compress.Compression;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Envelope is formed in the following Order:
 * 1、Total length of Envelope: 4 bytes
 * 2、Total length of Header: 4 bytes
 * 3、Fix length of Header: 4 bytes
 * 4、Header Data
 * 5、Body Data
 * <p>
 * +-----------+---------- +--------------+-------------+------------+
 * | TotalLen  | HeaderLen | FixHeaderLen | Header Data | Body Data  |
 * | (4 bytes) | (4 bytes) |  (4 bytes)   | (variable)  | (variable) |
 * +-----------+----------+---------------+-------------+------------+
 * <p>
 * Especial: Total length Reflected in the network framework (eg: Netty)
 *
 * @see
 */
@ToString
public class VirtueCodec implements Codec {

    private static final Logger logger = LoggerFactory.getLogger(VirtueCodec.class);

    private final Class<?> encodedClass;

    private final Class<?> decodedClass;

    private Constructor<? extends VirtueEnvelope> allArgsConstructor;

    private Constructor<? extends VirtueEnvelope> noArgsConstructor;

    public VirtueCodec(Class<? extends VirtueEnvelope> encodedClass, Class<? extends VirtueEnvelope> decodedClass) {
        this.encodedClass = encodedClass;
        this.decodedClass = decodedClass;
        findDecodedConstructor();
    }

    @Override
    public byte[] encode(Object message) throws CodecException {
        try {
            if (message instanceof Request request) {
                return encodeRequest(request);
            } else {
                Response response = (Response) message;
                return encodeResponse(response);
            }
        } catch (Exception e) {
            logger.error("Encode message fail", e);
            throw new CodecException(e);
        }
    }

    @Override
    public Object decode(byte[] bytes) throws CodecException {
        try {
            ByteReader byteReader = ByteReader.newReader(bytes);
            byte envelopeType = byteReader.readByte();
            Mode mode = ModeContainer.getMode(Key.ENVELOPE, envelopeType);
            if (mode.name().equals(Components.Envelope.REQUEST)) {
                return decodeRequest(byteReader.readBytes(byteReader.readableBytes()));
            } else {
                return decodeResponse(byteReader.readBytes(byteReader.readableBytes()));
            }
        } catch (Exception e) {
            logger.error("Decode message fail", e);
            throw new CodecException(e);
        }
    }

    private byte[] encodeRequest(Request request) {
        ByteWriter byteWriter = ByteWriter.newWriter();
        Mode requestMoe = ModeContainer.getMode(Key.ENVELOPE, Components.Envelope.REQUEST);
        byteWriter.writeByte(requestMoe.type());
        byteWriter.writeLong(request.id());
        byteWriter.writeBoolean(request.oneway());
        String url = request.url().toString();
        byteWriter.writeInt(url.length());
        byteWriter.writeCharSequence(url);
        byte[] message = encodeRequestMessage(request);
        byteWriter.writeBytes(message);
        return byteWriter.toBytes();
    }

    private Request decodeRequest(byte[] bytes) {
        ByteReader byteReader = ByteReader.newReader(bytes);
        long id = byteReader.readLong();
        boolean oneway = byteReader.readBoolean();
        int urlLength = byteReader.readInt();
        CharSequence urlStr = byteReader.readCharSequence(urlLength);
        URL url = URL.valueOf(String.valueOf(urlStr));
        Request request = new Request();
        request.id(id);
        request.oneway(oneway);
        request.url(url);
        Object message = decodeEnvelope(byteReader.readBytes(byteReader.readableBytes()));
        request.message(message);
        return request;
    }

    private byte[] encodeResponse(Response response) {
        ByteWriter byteWriter = ByteWriter.newWriter();
        Mode responseMode = ModeContainer.getMode(Key.ENVELOPE, Components.Envelope.RESPONSE);
        byteWriter.writeByte(responseMode.type());
        byteWriter.writeByte(response.code());
        String url = response.url().toString();
        byteWriter.writeInt(url.length());
        byteWriter.writeCharSequence(url);
        byte[] message = encodeResponseMessage(response);
        byteWriter.writeBytes(message);
        return byteWriter.toBytes();
    }

    private Response decodeResponse(byte[] bytes) {
        ByteReader byteReader = ByteReader.newReader(bytes);
        byte code = byteReader.readByte();
        int urlLength = byteReader.readInt();
        CharSequence urlStr = byteReader.readCharSequence(urlLength);
        URL url = URL.valueOf(String.valueOf(urlStr));
        Response response = new Response();
        response.code(code);
        response.url(url);
        Object message = decodeEnvelope(byteReader.readBytes(byteReader.readableBytes()));
        response.message(message);
        return response;
    }

    private byte[] encodeRequestMessage(Request request) {
        Object message = request.message();
        if (message instanceof VirtueEnvelope virtueEnvelope) {
            return encodeEnvelope(virtueEnvelope);
        } else {
            throw new IllegalArgumentException("The current encoder only supports requests for the virtue protocol");
        }
    }

    private byte[] encodeResponseMessage(Response response) {
        Object message = response.message();
        if (message instanceof VirtueEnvelope virtueEnvelope) {
            return encodeEnvelope(virtueEnvelope);
        } else {
            throw new IllegalArgumentException("The current encoder only supports requests for the virtue protocol");
        }
    }

    @SuppressWarnings("unchecked")
    private void findDecodedConstructor() {
        try {
            allArgsConstructor = (Constructor<? extends VirtueEnvelope>) decodedClass.getConstructor(Header.class, Object.class);
        } catch (NoSuchMethodException e) {
            try {
                noArgsConstructor = (Constructor<? extends VirtueEnvelope>) decodedClass.getConstructor();
            } catch (NoSuchMethodException ex) {
                logger.error("Can't find available Constructor,Need Envelope() or Envelope(Header,Object)", e);
            }
        }
    }

    private byte[] encodeEnvelope(VirtueEnvelope virtueEnvelope) throws CodecException {
        ByteWriter writer = ByteWriter.newWriter();
        try {
            byte[] fixHeaderBytes = encodeFixHeader(virtueEnvelope);
            byte[] extendHeaderBytes = encodeExtendHeader(virtueEnvelope);
            byte[] bodyBytes = encodeBody(virtueEnvelope);
            writer.writeInt(virtueEnvelope.header().getLength());
            writer.writeInt(fixHeaderBytes.length);
            writer.writeBytes(fixHeaderBytes);
            writer.writeBytes(extendHeaderBytes);
            writer.writeBytes(bodyBytes);
            return writer.toBytes();
        } catch (Exception e) {
            logger.error(this.getClass().getSimpleName() + " encode Error", e);
            throw new CodecException(e);
        }
    }

    private VirtueEnvelope decodeEnvelope(byte[] bytes) throws CodecException {
        ByteReader reader = ByteReader.newReader(bytes);
        try {
            int totalHeaderLength = reader.readInt();
            int fixHeaderLength = reader.readInt();
            byte[] fixHeaderBytes = reader.readBytes(fixHeaderLength);
            byte[] extendHeaderBytes = reader.readBytes(totalHeaderLength - fixHeaderLength);
            byte[] bodyBytes = reader.readBytes(reader.readableBytes());
            Header header = decodeHeader(fixHeaderBytes, extendHeaderBytes);
            Object body = decodeBody(header, bodyBytes);
            return createEnvelope(header, body);
        } catch (Exception e) {
            logger.error(this.getClass().getSimpleName() + " decode Error", e);
            throw new CodecException(e);
        }
    }

    private byte[] encodeFixHeader(VirtueEnvelope virtueEnvelope) {
        return virtueEnvelope.header().fixDataToBytes();
    }

    private byte[] encodeExtendHeader(VirtueEnvelope virtueEnvelope) {
        Header header = virtueEnvelope.header();
        header.addExtendData(Key.BODY_TYPE, virtueEnvelope.getBody().getClass().getName());
        return header.extendDataToBytes();
    }

    private byte[] encodeBody(VirtueEnvelope virtueEnvelope) {
        Serializer serializer = virtueEnvelope.header().serializer();
        byte[] bytes = serializer.serialize(virtueEnvelope.getBody());
        Compression compression = virtueEnvelope.header().compression();
        logger.debug("{}: [serializer: {}],[compression: {}]", this.getClass().getSimpleName(), serializer.getClass().getSimpleName(), compression.getClass().getSimpleName());
        return compression.compress(bytes);
    }

    @SuppressWarnings("unchecked")
    private Header decodeHeader(byte[] fixHeaderBytes, byte[] extendHeaderBytes) {
        Header header = decodeFixHeader(fixHeaderBytes);
        HashMap<String, String> extendData = (HashMap<String, String>) header.serializer().deserialize(extendHeaderBytes, HashMap.class);
        header.addExtendData(extendData);
        return header;
    }

    private Header decodeFixHeader(byte[] fixHeaderBytes) {
        return VirtueHeader.parse(fixHeaderBytes);
    }

    private Object decodeBody(Header header, byte[] bodyBytes) {
        Serializer serializer = header.serializer();
        Class<?> bodyType;
        try {
            bodyType = Class.forName(header.getExtendData(Key.BODY_TYPE));
        } catch (ClassNotFoundException e) {
            bodyType = Object.class;
        }
        Compression compression = header.compression();
        byte[] decompress = compression.decompress(bodyBytes);
        logger.debug("{}: [deserializer: {}],[decompression: {}]", this.getClass().getSimpleName(), serializer.getClass().getSimpleName(), compression.getClass().getSimpleName());
        return serializer.deserialize(decompress, bodyType);
    }

    private VirtueEnvelope createEnvelope(Header header, Object body) {
        VirtueEnvelope virtueEnvelope;
        try {
            if(allArgsConstructor!=null){
               virtueEnvelope = allArgsConstructor.newInstance(header,body);
            }else {
                virtueEnvelope = noArgsConstructor.newInstance();
                virtueEnvelope.setHeader(header);
                virtueEnvelope.setBody(body);
            }
        } catch (Exception e) {
            logger.error("Create "+decodedClass.getSimpleName()+" Instance Error ", e);
            throw new CodecException(e);
        }
        return virtueEnvelope;
    }
}
