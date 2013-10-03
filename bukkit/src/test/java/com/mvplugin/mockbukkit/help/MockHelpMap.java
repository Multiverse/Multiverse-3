package com.mvplugin.mockbukkit.help;

import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

import java.util.Collection;
import java.util.List;

public class MockHelpMap implements HelpMap {

    @Override
    public HelpTopic getHelpTopic(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<HelpTopic> getHelpTopics() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addTopic(HelpTopic helpTopic) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clear() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void registerHelpTopicFactory(Class<?> aClass, HelpTopicFactory<?> helpTopicFactory) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> getIgnoredPlugins() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
