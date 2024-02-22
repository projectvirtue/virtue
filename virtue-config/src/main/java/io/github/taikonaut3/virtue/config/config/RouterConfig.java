package io.github.taikonaut3.virtue.config.config;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.intellij.lang.annotations.Language;

@Accessors(fluent = true, chain = true)
@Getter
@ToString
public class RouterConfig {

    private final String urlRegex;

    private String matchTargetRegex;

    public RouterConfig(@Language("RegExp") String urlRegex) {
        this.urlRegex = urlRegex;
    }

    public RouterConfig match(@Language("RegExp") String matchTargetRegex) {
        this.matchTargetRegex = matchTargetRegex;
        return this;
    }
}
