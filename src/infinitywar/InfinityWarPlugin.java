package infinitywar;

import arc.Events;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.game.EventType.WorldLoadEndEvent;
import mindustry.mod.*;

public class InfinityWarPlugin extends Plugin {

    @Override
    public void init() {
        Events.on(WorldLoadEndEvent.class, (e) -> {
            while (true) {
                Vars.world.tiles.eachTile(tile -> {
                    for (var item : Items.serpuloItems) {
                        tile.build.items().add(item, 1000);
                    }
                    for (var item : Items.erekirItems) {
                        tile.build.items().add(item, 1000);
                    }
                });
            }
        });
    }

    @Override
    public void registerServerCommands(CommandHandler handler) {

    }

    @Override
    public void registerClientCommands(CommandHandler handler) {

    }
}
