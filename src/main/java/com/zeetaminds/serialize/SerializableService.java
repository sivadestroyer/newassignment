package com.zeetaminds.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerializableService<T> {
    void serialize(T obj, OutputStream os) throws IOException;
    T deserialize(InputStream is) throws IOException, ClassNotFoundException;
}
