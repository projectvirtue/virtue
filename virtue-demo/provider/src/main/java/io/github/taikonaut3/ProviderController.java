package io.github.taikonaut3;

import io.github.taikonaut3.model2.ParentObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProviderController {

    @GetMapping("hello")
    public String hello(@RequestParam("world") String world) {
        return "hello" + world;
    }

    @PostMapping("hello/list")
    public List<ParentObject> list(@RequestBody List<ParentObject> list) {
        return ParentObject.getObjList();
    }

    @PostMapping("hello/list/{id}/name/{name1}")
    public String path(@PathVariable("id") String id, @PathVariable("name1") String name) {
        return id + name;
    }
}
