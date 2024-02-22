package io.github.taikonaut3.virtue.transport;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.url.URL;

import java.io.Serializable;


public interface Envelope extends Serializable {

    long serialVersionUID = 1L;

    URL url();

    default Long id() {
        return url().getLongParameter(Key.UNIQUE_ID);
    }

}
