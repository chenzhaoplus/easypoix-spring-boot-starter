package com.itguoguo.easypoix.starter.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MockServletOutputStream extends ServletOutputStream {

    private FileOutputStream fos;

    public MockServletOutputStream(String fileName) {
        new MockServletOutputStream(new File(fileName));
    }

    public MockServletOutputStream(File file) {
        try {
            this.fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new RuntimeException("do not support this method");
    }

    @Override
    public void write(int b) throws IOException {
        fos.write(b);
    }

    @Override
    public void close() {
        try {
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
