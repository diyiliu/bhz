package com.tiza.xgdl.command;

import com.tiza.xgdl.util.CommandConfig;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-07-17 14:34)
 * Version: 1.0
 * Updated:
 */
public class CommandFactory {
    private static final ConcurrentHashMap<Integer, BaseCommand> baseCommand = new ConcurrentHashMap<Integer, BaseCommand>();


    public static BaseCommand getCommand(int commandId) {
        return baseCommand.get(commandId);
    }

    public static void init() {
        CommandConfig[] config = CommandConfig.values();
        for (CommandConfig val : config) {
            add(val.getKey(), val.getValue());
        }
    }

    public static void clear() {
        baseCommand.clear();
    }

    public static void add(int key, Class command) {
        try {
            baseCommand.put(key, (BaseCommand) command.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
