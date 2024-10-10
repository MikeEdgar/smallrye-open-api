package io.smallrye.openapi.internal.models.headers;

public class Header extends AbstractHeader {

    public Header() {
        super();
        /*
         * Required until https://github.com/eclipse/microprofile-open-api/issues/661 is resolved.
         */
        setStyle(Style.SIMPLE);
    }

}
