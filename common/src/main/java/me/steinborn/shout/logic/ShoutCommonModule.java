package me.steinborn.shout.logic;

import com.google.inject.AbstractModule;

public class ShoutCommonModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ShoutPlugin.class).asEagerSingleton();
    }
}
