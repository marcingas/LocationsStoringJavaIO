package com.MarcinDev;

import java.io.*;
import java.nio.channels.ScatteringByteChannel;
import java.util.*;

public class Locations implements Map<Integer, Location> {
    //create locations object to store our data
    private static Map<Integer, Location> locations = new LinkedHashMap<Integer, Location>();
    private static Map<Integer, IndexRecord> index = new LinkedHashMap<>();

    public static void main(String[] args) throws IOException {
        try (RandomAccessFile rao = new RandomAccessFile("locations_random.dat","rwd")) {
            rao.writeInt(locations.size());
            int indexSize = locations.size() * 3 * Integer.BYTES;
            int locationStart = (int) (indexSize + rao.getFilePointer() + Integer.BYTES);
            rao.writeInt(locationStart);
            long indexStart = rao.getFilePointer();

            int startPointer = locationStart;
            rao.seek(startPointer);

            for(Location location : locations.values()){
                rao.writeInt(location.getLocationId());
                rao.writeUTF(location.getDescription());

                StringBuilder builder = new StringBuilder();
                for(String direction : location.getExits().keySet()){
                    if(!direction.equalsIgnoreCase("Q")){
                        builder.append(direction);
                        builder.append(",");
                        builder.append(location.getExits().get(direction));
                        builder.append(",");

                    }
                }
                rao.writeUTF(builder.toString());
                IndexRecord record = new IndexRecord(startPointer, (int)(rao.getFilePointer() - startPointer));
                index.put(location.getLocationId(), record);
                startPointer = (int) rao.getFilePointer();
            }
        }
    }

    static {
        try (ObjectInputStream locFile =
                     new ObjectInputStream(new BufferedInputStream(new FileInputStream("locations.dat")))) {
            boolean eof = false;
            while (!eof) {
                try {
                    Location location = (Location) locFile.readObject();
                    System.out.println("read location " + location.getLocationId() + " : " + location.getDescription());
                    System.out.println("found " + location.getExits().size() + " exits");
                    locations.put(location.getLocationId(), location);
                } catch (EOFException e) {
                    eof = true;
                }
            }
        } catch (InvalidClassException e) {
            System.out.println("Invalid Class Exception " + e.getMessage());
        } catch (IOException io) {
            System.out.println("IO Exception " + io.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException " + e.getMessage());
        }
    }

    @Override
    public int size() {
        return locations.size();
    }

    @Override
    public boolean isEmpty() {
        return locations.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return locations.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return locations.containsValue(value);
    }

    @Override
    public Location get(Object key) {
        return locations.get(key);
    }

    @Override
    public Location put(Integer key, Location value) {
        return locations.put(key, value);
    }

    @Override
    public Location remove(Object key) {
        return locations.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Location> m) {

    }

    @Override
    public void clear() {
        locations.clear();

    }

    @Override
    public Set<Integer> keySet() {
        return locations.keySet();
    }

    @Override
    public Collection<Location> values() {
        return locations.values();
    }

    @Override
    public Set<Entry<Integer, Location>> entrySet() {
        return locations.entrySet();
    }
}
