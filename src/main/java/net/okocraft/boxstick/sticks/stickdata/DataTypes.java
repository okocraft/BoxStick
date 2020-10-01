package net.okocraft.boxstick.sticks.stickdata;

import java.util.Optional;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public final class DataTypes {

    private DataTypes() {
    }

    public static final PersistentDataType<Byte, Boolean> BOOL = new PersistentDataType<Byte, Boolean>() {

        @Override
        public Class<Byte> getPrimitiveType() {
            return Byte.class;
        }

        @Override
        public Class<Boolean> getComplexType() {
            return Boolean.class;
        }

        @Override
        public Byte toPrimitive(Boolean complex, PersistentDataAdapterContext context) {
            return (byte) (complex.booleanValue() ? 1 : 0);
        }

        @Override
        public Boolean fromPrimitive(Byte primitive, PersistentDataAdapterContext context) {
            return Optional.ofNullable(primitive).orElse((byte) 0) == 1;
        }
    };

    public static final PersistentDataType<int[], Vector> VECTOR = new PersistentDataType<>() {

        @Override
        public Class<int[]> getPrimitiveType() {
            return int[].class;
        }

        @Override
        public Class<Vector> getComplexType() {
            return Vector.class;
        }

        @Override
        public int[] toPrimitive(Vector complex, PersistentDataAdapterContext context) {
            return new int[] { complex.getBlockX(), complex.getBlockY(), complex.getBlockZ() };
        }

        @Override
        public Vector fromPrimitive(int[] primitive, PersistentDataAdapterContext context) {
            switch (primitive.length) {
                case 3: return new Vector(primitive[0], primitive[1], primitive[2]);
                case 2: return new Vector(primitive[0], primitive[1], 0);
                case 1: return new Vector(primitive[0], 0, 0);
                default: return new Vector();
            }
        }
    };

    public static final PersistentDataType<PersistentDataContainer, StickData> STICK_DATA = new PersistentDataType<>() {

        @Override
        public Class<PersistentDataContainer> getPrimitiveType() {
            return PersistentDataContainer.class;
        }

        @Override
        public Class<StickData> getComplexType() {
            return StickData.class;
        }

        @Override
        public PersistentDataContainer toPrimitive(StickData stickData,
                PersistentDataAdapterContext persistentDataAdapterContext) {
            return stickData.deserialize(persistentDataAdapterContext.newPersistentDataContainer());
        }

        @Override
        public StickData fromPrimitive(PersistentDataContainer persistentDataContainer,
                PersistentDataAdapterContext persistentDataAdapterContext) {
            return StickData.serialize(persistentDataContainer);
        }
    };
}