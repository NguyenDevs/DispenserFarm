package com.NguyenDevs.dispenserfarm;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.Map;

public class Dispenserfarm implements ModInitializer {
    private static final Map<Item, Block> PLANTABLE_ITEMS = Util.make(new LinkedHashMap<>(), map -> {
        map.put(Items.WHEAT_SEEDS, Blocks.WHEAT);
        map.put(Items.POTATO, Blocks.POTATOES);
        map.put(Items.CARROT, Blocks.CARROTS);
        map.put(Items.BEETROOT_SEEDS, Blocks.BEETROOTS);
        map.put(Items.MELON_SEEDS, Blocks.MELON_STEM);
        map.put(Items.PUMPKIN_SEEDS, Blocks.PUMPKIN_STEM);

        //map.put(Items.COCOA_BEANS, Blocks.COCOA);

        map.put(Items.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM);
        map.put(Items.RED_MUSHROOM, Blocks.RED_MUSHROOM);
        map.put(Items.CRIMSON_FUNGUS, Blocks.CRIMSON_FUNGUS);
        map.put(Items.WARPED_FUNGUS, Blocks.WARPED_FUNGUS);

        map.put(Items.OAK_SAPLING, Blocks.OAK_SAPLING);
        map.put(Items.BIRCH_SAPLING, Blocks.BIRCH_SAPLING);
        map.put(Items.SPRUCE_SAPLING, Blocks.SPRUCE_SAPLING);
        map.put(Items.JUNGLE_SAPLING, Blocks.JUNGLE_SAPLING);
        map.put(Items.ACACIA_SAPLING, Blocks.ACACIA_SAPLING);
        map.put(Items.DARK_OAK_SAPLING, Blocks.DARK_OAK_SAPLING);
        map.put(Items.CHERRY_SAPLING, Blocks.CHERRY_SAPLING);
        map.put(Items.MANGROVE_PROPAGULE, Blocks.MANGROVE_PROPAGULE);

        map.put(Items.AZALEA, Blocks.AZALEA);
        map.put(Items.FLOWERING_AZALEA, Blocks.FLOWERING_AZALEA);

        map.put(Items.POPPY, Blocks.POPPY);
        map.put(Items.DANDELION, Blocks.DANDELION);
        map.put(Items.BLUE_ORCHID, Blocks.BLUE_ORCHID);
        map.put(Items.ALLIUM, Blocks.ALLIUM);
        map.put(Items.AZURE_BLUET, Blocks.AZURE_BLUET);
        map.put(Items.RED_TULIP, Blocks.RED_TULIP);
        map.put(Items.ORANGE_TULIP, Blocks.ORANGE_TULIP);
        map.put(Items.WHITE_TULIP, Blocks.WHITE_TULIP);
        map.put(Items.PINK_TULIP, Blocks.PINK_TULIP);
        map.put(Items.OXEYE_DAISY, Blocks.OXEYE_DAISY);
        map.put(Items.CORNFLOWER, Blocks.CORNFLOWER);
        map.put(Items.LILY_OF_THE_VALLEY, Blocks.LILY_OF_THE_VALLEY);
        map.put(Items.SUNFLOWER, Blocks.SUNFLOWER);
        map.put(Items.LILAC, Blocks.LILAC);
        map.put(Items.ROSE_BUSH, Blocks.ROSE_BUSH);
        map.put(Items.PEONY, Blocks.PEONY);

        map.put(Items.TORCHFLOWER_SEEDS, Blocks.TORCHFLOWER_CROP);

        map.put(Items.BAMBOO, Blocks.BAMBOO_SAPLING);
        map.put(Items.SUGAR_CANE, Blocks.SUGAR_CANE);
        map.put(Items.GLOW_BERRIES, Blocks.CAVE_VINES);
        map.put(Items.SWEET_BERRIES, Blocks.SWEET_BERRY_BUSH);
        map.put(Items.NETHER_WART, Blocks.NETHER_WART);

        map.put(Items.CHORUS_FLOWER, Blocks.CHORUS_FLOWER);
    });

    @Override
    public void onInitialize() {
        PLANTABLE_ITEMS.forEach((item, block) -> {
            DispenserBehavior behavior = new ItemDispenserBehavior() {
                @Override
                protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                    World world = pointer.world();
                    BlockPos pos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
                    Direction facing = pointer.state().get(DispenserBlock.FACING);
                    BlockState existing = world.getBlockState(pos);

                    if (PLANTABLE_ITEMS.containsValue(existing.getBlock())) {
                        return stack;
                    }

                    if (block instanceof TallFlowerBlock) {
                        if (canPlace(block, world, pos, pointer.pos(), facing)) {
                            if (world.getBlockState(pos.up()).isAir()) {
                                world.setBlockState(pos, block.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.LOWER));
                                world.setBlockState(pos.up(), block.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.UPPER));
                                stack.decrement(1);
                                return stack;
                            }
                        }
                        return stack;
                       // return super.dispenseSilently(pointer, stack);
                    }

                    if (canPlace(block, world, pos, pointer.pos(), facing)) {
                        if (block == Blocks.BAMBOO_SAPLING) {
                            world.setBlockState(pos, Blocks.BAMBOO_SAPLING.getDefaultState());
                            stack.decrement(1);
                            return stack;
                        }


                        if (block == Blocks.CAVE_VINES) {
                            world.setBlockState(pos, Blocks.CAVE_VINES.getDefaultState());
                            stack.decrement(1);
                            return stack;
                        }

                        if (block == Blocks.SUGAR_CANE) {
                            if (facing.getAxis().isVertical()) {
                                return stack;
                            }
                            world.setBlockState(pos, Blocks.SUGAR_CANE.getDefaultState());
                            stack.decrement(1);
                            return stack;
                        }

                        if (block == Blocks.AZALEA || block == Blocks.FLOWERING_AZALEA) {
                            world.setBlockState(pos, block.getDefaultState());
                            stack.decrement(1);
                            return stack;
                        }

                        world.setBlockState(pos, block.getDefaultState());
                        stack.decrement(1);
                        return stack;
                    }
                    return stack;
                   // return super.dispenseSilently(pointer, stack);
                }
            };
            DispenserBlock.registerBehavior(item, behavior);
        });

        DispenserBlock.registerBehavior(Items.BONE_MEAL, new ItemDispenserBehavior() {
            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                World world = pointer.world();
                BlockPos pos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();

                if (PLANTABLE_ITEMS.containsValue(block) || block == Blocks.BAMBOO) {
                    if (block == Blocks.CAVE_VINES || block == Blocks.CAVE_VINES_PLANT) {
                        if (!state.get(CaveVines.BERRIES)) {
                            world.setBlockState(pos, state.with(CaveVines.BERRIES, true));
                            spawnHappyParticles(world, pos);
                            stack.decrement(1);
                            return stack;
                        }
                        if (!state.get(CaveVinesBodyBlock.BERRIES)) {
                            world.setBlockState(pos, state.with(CaveVinesBodyBlock.BERRIES, true));
                            spawnHappyParticles(world, pos);
                            stack.decrement(1);
                            return stack;
                        }
                        else if (world instanceof ServerWorld serverWorld) {
                            int dropCount = world.random.nextInt(2) + 1;
                            ItemStack berries = new ItemStack(Items.GLOW_BERRIES, dropCount);
                            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, berries);
                            itemEntity.setToDefaultPickupDelay();
                            world.spawnEntity(itemEntity);
                            spawnHappyParticles(world, pos);
                            stack.decrement(1);
                            return stack;
                        }
                        return stack;
                    }

                    if (block instanceof Fertilizable fertilizable && world instanceof ServerWorld serverWorld) {
                        if (fertilizable.isFertilizable(world, pos, state)) {
                            if (fertilizable.canGrow(world, world.random, pos, state)) {
                                fertilizable.grow(serverWorld, world.random, pos, state);
                                spawnHappyParticles(world, pos);
                                stack.decrement(1);
                                return stack;
                            }
                        }
                        return stack;
                    }

                    if (block == Blocks.SUGAR_CANE) {
                        int height = 1;
                        BlockPos current = pos;
                        while (world.getBlockState(current.down()).getBlock() == Blocks.SUGAR_CANE) {
                            current = current.down();
                        }
                        while (world.getBlockState(current).getBlock() == Blocks.SUGAR_CANE && height <= 3) {
                            height++;
                            current = current.up();
                        }
                        if (height > 3 || !world.getBlockState(current).isAir()) {
                            return stack;
                        }
                        world.setBlockState(current, Blocks.SUGAR_CANE.getDefaultState());
                        spawnHappyParticles(world, pos);
                        stack.decrement(1);
                        return stack;
                    }
                    if (block == Blocks.NETHER_WART) {
                        int age = state.get(NetherWartBlock.AGE);
                        if (age < NetherWartBlock.MAX_AGE && world instanceof ServerWorld serverWorld) {
                            world.setBlockState(pos, state.with(NetherWartBlock.AGE, age + 1));
                            spawnHappyParticles(world, pos);
                            stack.decrement(1);
                            return stack;
                        }
                        return stack;
                    }
                    // Non-fertilizable blocks (e.g., flowers, azalea, tall flowers, chorus flower, mushrooms)
                    return stack;
                }
                return stack;
                // If no plantable block, eject bone meal as an item
                //return super.dispenseSilently(pointer, stack);
            }
        });
    }

    private void spawnHappyParticles(World world, BlockPos pos) {
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    8, 0.3, 0.3, 0.3, 0.0);
        }
    }

    private boolean canPlace(Block block, World world, BlockPos pos, BlockPos dispenserPos, Direction facing) {
        BlockState groundState = world.getBlockState(pos.down());
        Block ground = groundState.getBlock();
        BlockState targetState = world.getBlockState(pos);

        if (!targetState.isAir()) return false;

        if (block == Blocks.WHEAT || block == Blocks.CARROTS || block == Blocks.POTATOES || block == Blocks.BEETROOTS
                || block == Blocks.MELON_STEM || block == Blocks.PUMPKIN_STEM || block == Blocks.TORCHFLOWER_CROP) {
            return ground instanceof FarmlandBlock;
        }

        if (block == Blocks.BROWN_MUSHROOM || block == Blocks.RED_MUSHROOM) {
            return ground == Blocks.MYCELIUM;
        }

        if (block == Blocks.CRIMSON_FUNGUS || block == Blocks.WARPED_FUNGUS) {
            return ground == Blocks.CRIMSON_NYLIUM || ground == Blocks.WARPED_NYLIUM;
        }

        if (block == Blocks.NETHER_WART) {
            return ground == Blocks.SOUL_SAND;
        }

        if (block == Blocks.CAVE_VINES) {
            BlockState above = world.getBlockState(pos.up());
            return above.isOpaque() && above.isSolidBlock(world, pos.up());
        }

        if (block == Blocks.SUGAR_CANE) {
            if (facing.getAxis().isVertical()) return false;
            BlockPos soil = pos.down();
            boolean validSoil = ground == Blocks.SAND || ground == Blocks.ROOTED_DIRT || ground == Blocks.MUD
                    || ground == Blocks.COARSE_DIRT || ground == Blocks.PODZOL || ground == Blocks.DIRT || ground == Blocks.GRASS_BLOCK;
            boolean hasWater = false;
            for (Direction dir : Direction.Type.HORIZONTAL) {
                if (world.getBlockState(soil.offset(dir)).getBlock() == Blocks.WATER) {
                    hasWater = true;
                    break;
                }
            }
            return validSoil && hasWater;
        }

        if (block == Blocks.CHORUS_FLOWER) {
            return ground == Blocks.END_STONE;
        }

        if (block instanceof FlowerBlock || block instanceof SaplingBlock || block == Blocks.BAMBOO_SAPLING || block == Blocks.SWEET_BERRY_BUSH
                || block == Blocks.AZALEA || block == Blocks.FLOWERING_AZALEA) {
            return ground == Blocks.SAND || ground == Blocks.ROOTED_DIRT || ground == Blocks.MUD
                    || ground == Blocks.COARSE_DIRT || ground == Blocks.PODZOL || ground == Blocks.DIRT || ground == Blocks.GRASS_BLOCK;
        }

        if (block instanceof TallFlowerBlock) {
            return world.getBlockState(pos).isAir() && world.getBlockState(pos.up()).isAir()
                    && (ground == Blocks.SAND || ground == Blocks.ROOTED_DIRT || ground == Blocks.MUD
                    || ground == Blocks.COARSE_DIRT || ground == Blocks.PODZOL || ground == Blocks.DIRT || ground == Blocks.GRASS_BLOCK);
        }

        return false;
    }
}