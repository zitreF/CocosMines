package me.cocos.cocosmines.service;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.argument.impl.CreateArgument;
import me.cocos.cocosmines.argument.impl.PanelArgument;
import me.cocos.cocosmines.argument.impl.RemoveArgument;
import me.cocos.cocosmines.argument.impl.TeleportArgument;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class ArgumentService {

    private final Map<String, Argument> arguments;

    public ArgumentService(CocosMines plugin) {
        this.arguments = new HashMap<>();
        Stream.of(
                new CreateArgument(plugin.getMineService(), plugin.getModificationService()),
                new PanelArgument(plugin.getMineService().getMines()),
                new RemoveArgument(plugin.getMineService()),
                new TeleportArgument(plugin.getMineService())
        ).forEach(command -> arguments.put(command.getName(), command));
    }
    public Argument getArgumentByName(String name) {
        return this.arguments.get(name);
    }

    public Collection<Argument> getArguments() {
        return Collections.unmodifiableCollection(this.arguments.values());
    }
}
