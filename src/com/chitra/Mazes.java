package com.chitra;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.*;

/**
 * Created by chitrakakkar on 4/9/16.
 */
public class Mazes
{



    HashMap<Integer,Integer> MazeCoordinates = new HashMap<>();
    HashMap<Integer,List<Integer>> MazeCoordinates2 = new HashMap<>();
    public Mazes()
    {
        CreateMaze();
    }
    //mazes on only two corner of the wall
        protected HashMap<Integer,Integer> CreateMaze()
        {
            MazeCoordinates.put(0,0);
            MazeCoordinates.put(9,9);
            return MazeCoordinates;

        }
// mazes on all the corner of the wall-? did not use it-> will use it in the next version
    //just for the reference.
    protected HashMap<Integer,List<Integer>> CreateMaze2()
    {
        List<Integer> ValueForKey1 = new ArrayList<>();
        List<Integer> ValueForKey2 = new ArrayList<>();
        ValueForKey1.add(0);
        ValueForKey1.add(9);
        ValueForKey2.add(0);
        ValueForKey2.add(9);
        MazeCoordinates2.put(0,ValueForKey1);
        MazeCoordinates2.put(9,ValueForKey2);
        return MazeCoordinates2;
    }
    public HashMap<Integer, Integer> getMazeCoordinates() {
        return MazeCoordinates;
    }

    public void setMazeCoordinates(HashMap<Integer, Integer> mazeCoordinates) {
        MazeCoordinates = mazeCoordinates;}

}
