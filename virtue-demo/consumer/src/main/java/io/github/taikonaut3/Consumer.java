package io.github.taikonaut3;

import io.github.taikonaut3.model.ParentObject;
import io.github.taikonaut3.virtue.config.annotation.Config;
import io.github.taikonaut3.virtue.config.annotation.RemoteCaller;
import io.github.taikonaut3.virtue.rpc.virtue.config.VirtueCall;
import io.github.taikonaut3.virtue.rpc.virtue.envelope.VirtueResponse;
import io.github.taikonaut3.virtue.transport.Response;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static io.github.taikonaut3.virtue.common.constant.Components.Serialize.JSON;
import static io.github.taikonaut3.virtue.common.constant.Components.Serialize.MSGPACK;

@RemoteCaller(value = "provider")
public interface Consumer {

    @Config(filters = {"filter2"}, serialize = JSON)
    @VirtueCall(service = "345", callMethod = "hello")
    String hello(String world);

    @Config(filters = {"filter1"},serialize = JSON)
    @VirtueCall(service = "345", callMethod = "hello")
    CompletableFuture<String> helloAsync(String world);

    @VirtueCall(service = "345", callMethod = "hello", config = @Config(filters = "test"))
    CompletableFuture<Response> helloResp(String world);

    @VirtueCall(service = "345", callMethod = "hello", config = @Config(filters = "test"))
    CompletableFuture<VirtueResponse> helloDynamicRes(String world);

    @VirtueCall(service = "345",callMethod = "list",config = @Config(serialize = MSGPACK))
    List<ParentObject> list(List<ParentObject> list);

}
