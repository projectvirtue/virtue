package io.github.taikonaut3;

import io.github.taikonaut3.model.ParentObject;
import io.github.taikonaut3.virtue.config.annotation.Config;
import io.github.taikonaut3.virtue.config.annotation.Options;
import io.github.taikonaut3.virtue.config.annotation.RemoteCaller;
import io.github.taikonaut3.virtue.rpc.http1_1.config.HttpCall;
import io.github.taikonaut3.virtue.rpc.http1_1.config.HttpMethod;
import io.github.taikonaut3.virtue.rpc.http1_1.config.Param;
import io.github.taikonaut3.virtue.rpc.http1_1.config.PathVariable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RemoteCaller("provider")
public interface HttpConsumer {


    @Options(retires = 0)
    @HttpCall(path = "hello", method = HttpMethod.GET)
    String hello(@Param("world") String world);

    @HttpCall(path = "hello/list", method = HttpMethod.POST)
    CompletableFuture<List<ParentObject>> list(List<ParentObject> list);

    @HttpCall(path = "hello/list/{id}/name/{name}", method = HttpMethod.POST, config = @Config(filters = {"filter1"}))
    String path(@PathVariable("id") String id, @PathVariable("name") String name);
}
