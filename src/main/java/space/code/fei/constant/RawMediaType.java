package space.code.fei.constant;

import okhttp3.MediaType;

public enum RawMediaType {

    TEXT_PLAIN(MediaType.parse("text/plain")),
    APPLICATION_JSON(MediaType.parse("application/json")),
    APPLICATION_XML(MediaType.parse("application/xml"));

    private MediaType name;

    public MediaType getName() {
        return name;
    }

    RawMediaType(MediaType name){
        this.name = name;
    }

}
