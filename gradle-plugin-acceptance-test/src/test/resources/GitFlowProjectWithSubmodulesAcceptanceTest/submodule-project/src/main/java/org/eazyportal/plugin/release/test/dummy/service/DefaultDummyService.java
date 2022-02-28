package org.eazyportal.plugin.release.test.dummy.service;

public class DefaultDummyService implements DummyService {

    @Override
    public String get() {
        return "Hello World!";
    }

}
