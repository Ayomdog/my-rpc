////package com.ayom.myrpc.serializer;
////
////import java.io.*;
////
////public class JdkSerializer implements Serializer{
////    /**
////     * 序列化
////     * @param obj
////     * @return
////     * @param <T>
////     * @throws IOException
////     */
////    @Override
////    public <T> byte[] serialize(T obj) throws IOException {
////        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
////        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
////        objectOutputStream.writeObject(obj);
////        objectOutputStream.close();
////        return outputStream.toByteArray();
////    }
////
////    /**
////     * 反序列化
////     * @param bytes
////     * @param clazz
////     * @return
////     * @param <T>
////     * @throws IOException
////     */
////    @Override
////    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
////        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
////        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
////        try{
////            return (T)objectInputStream.readObject();
////        } catch (ClassNotFoundException e) {
////            throw new RuntimeException(e);
////        }finally {
////            objectInputStream.close();
////        }
////    }
////}
//package com.ayom.myrpc.serializer;
//
//import java.io.*;
//
///**
// * JDK 序列化器
// */
//public class JdkSerializer implements Serializer {
//
//    /**
//     * 序列化
//     *
//     * @param object
//     * @param <T>
//     * @return
//     * @throws IOException
//     */
//    @Override
//    public <T> byte[] serialize(T object) throws IOException {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
////        try {
////            objectOutputStream.writeObject(object);
////            objectOutputStream.flush();
////            return outputStream.toByteArray();
////        } finally {
////            objectOutputStream.close();
////        }
//        objectOutputStream.writeObject(object);
//        objectOutputStream.close();
//        return outputStream.toByteArray();
//    }
//
//    /**
//     * 反序列化
//     *
//     * @param bytes
//     * @param type
//     * @param <T>
//     * @return
//     * @throws IOException
//     */
//    @Override
//    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
//        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//        try {
//            return (T) objectInputStream.readObject();
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        } finally {
//            objectInputStream.close();
//        }
//    }
//}
package com.ayom.myrpc.serializer;

import java.io.*;

/**
 * JDK 序列化器
 */
public class JdkSerializer implements Serializer {

    /**
     * 序列化
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return outputStream.toByteArray();
        }
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            try {
                @SuppressWarnings("unchecked")
                T result = (T) objectInputStream.readObject();
                return result;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to deserialize object", e);
            } catch (EOFException e) {
                throw new IOException("Incomplete data: EOF reached before object could be fully read", e);
            }
        }
    }
}
