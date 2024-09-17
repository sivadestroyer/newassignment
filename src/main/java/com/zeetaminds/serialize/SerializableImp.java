package com.zeetaminds.serialize;

import com.zeetaminds.serialize.SerializableService;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerializableImp<T> implements SerializableService<T> {

    private static final Logger logger = Logger.getLogger(SerializableImp.class.getName());

    @Override
    public void serialize(T obj, OutputStream os) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(os);
        out.writeObject(obj);
        logger.log(Level.INFO, "Serialized: {0}", obj.getClass().getSimpleName());
    }

    @Override
    public T deserialize(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(is);
        T obj = (T) in.readObject();
        logger.log(Level.INFO, "Deserialized: {0}", obj.getClass().getSimpleName());
        return obj;
    }
}
