package net.awakaxis.uno.block;

import net.awakaxis.uno.UNOBlockEntities;
import net.awakaxis.uno.UNOItems;
import net.awakaxis.uno.item.UnoCardItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CardDeckBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape NORTH_AABB = box(6, 0, 5, 10, 2, 11);
    private static final VoxelShape WEST_AABB = box(5, 0, 6, 11, 2, 10);

    public CardDeckBlock(BlockBehaviour.Properties properties) {super(properties);}

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        CardDeckBlockEntity cardDeck = (CardDeckBlockEntity) level.getBlockEntity(blockPos);
        if (cardDeck != null) {
            if (!player.isCrouching()) {
                cardDeck.decrementCardCount();
                RandomSource randomSource = RandomSource.create();
                ItemStack card = ((UnoCardItem)UNOItems.UNO_CARD).getWithIndex(randomSource.nextInt(9, 62));
                if (!player.addItem(card)) {
                    player.drop(card, false);
                }
            } else {
                cardDeck.resetCardCount();
            }
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction direction = blockPlaceContext.getHorizontalDirection();
        return defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof CardDeckBlockEntity cardDeckBlockEntity) {
            if (!level.isClientSide) {
                cardDeckBlockEntity.createAndDropItemStack();
            }
        }
        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch (blockState.getValue(FACING)) {
            case EAST, WEST -> WEST_AABB;
            default -> NORTH_AABB;
        };
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (!this.canSurvive(blockState, levelAccessor, blockPos)) {
            BlockEntity blockEntity = levelAccessor.getBlockEntity(blockPos);
            if (blockEntity instanceof CardDeckBlockEntity cardDeckBlockEntity) {
                cardDeckBlockEntity.createAndDropItemStack();
            }
            return Blocks.AIR.defaultBlockState();
        }
        return blockState;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return levelReader.getBlockState(blockPos.below()).isFaceSturdy(levelReader, blockPos.below(), Direction.UP);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CardDeckBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        // idk why this had a ticker, it really doesn't need one (maybe later though, for enforced game rules like a time limit on playing a card or something)
        return null;
//        return level.isClientSide ? null : createTickerHelper(blockEntityType, UNOBlockEntities.CARD_DECK_BLOCK_ENTITY_TYPE, CardDeckBlockEntity::tick);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
