package com.needyyy.app.constants.Blocks;

public final class GenricResponse <T>   {
    private T t;

    public GenricResponse(T t) {
        setObjects(t);
    }

    /**getObject methods will return the object of type T*/
    public T getObject ()    {
        return t;
    }

    /**this add the object to Genric class and create object of T class*/
    private void setObjects(T t)  {
        this.t = t;
    }
}
