package infinitywar;

import java.util.concurrent.atomic.AtomicBoolean;

import arc.Events;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.EventType.WorldLoadEndEvent;
import mindustry.mod.*;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.consumers.ConsumeLiquid;

public class InfinityWarPlugin extends Plugin {
    private volatile AtomicBoolean isFilling = new AtomicBoolean(false);

    @Override
    public void init() {
        Events.on(WorldLoadEndEvent.class, (e) -> {
            Timer.schedule(() -> {
                if (isFilling.get())
                    return;

                isFilling.set(true);

                for (var tile : Vars.world.tiles) {
                    var build = tile.build;
                    var block = tile.block();

                    if (build == null || block == null)
                        continue;

                    if (build.items == null)
                        continue;

                    for (var consumer : block.consumers) {
                        if (consumer instanceof ConsumeItems consumeItems) {
                            if (block.name.equals(Blocks.thoriumReactor.name)) {
                                for (var stack : consumeItems.items) {
                                    if (build.items.get(stack.item) < block.itemCapacity) {
                                        build.items.add(stack.item, block.itemCapacity);
                                        continue;
                                    }
                                }
                            }

                            for (var stack : consumeItems.items) {
                                if (build.items.get(stack.item) < 1000) {
                                    build.items.add(stack.item, 1000);
                                }
                            }
                        } else if ((consumer instanceof ConsumeLiquid consumeLiquid)) {
                            if (build.liquids.get(consumeLiquid.liquid) < block.liquidCapacity) {
                                build.liquids.add(consumeLiquid.liquid, block.liquidCapacity);
                            }

                        }
                    }
                    for (var item : Vars.content.items()) {
                        if (block.consumesItem(item)) {
                            if (build.items.get(item) < 1000) {
                                build.items.add(item, 1000);
                            }
                        }
                    }

                    for (var liquid : Vars.content.liquids()) {
                        if (block.consumesLiquid(liquid)) {
                            if (build.liquids.get(liquid) < block.liquidCapacity) {
                                build.liquids.add(liquid, block.liquidCapacity);
                            }
                        }
                    }
                }
                isFilling.set(false);
            }, 0, 1);
        });

    }

    @Override
    public void registerServerCommands(CommandHandler handler) {

    }

    @Override
    public void registerClientCommands(CommandHandler handler) {

    }
}
