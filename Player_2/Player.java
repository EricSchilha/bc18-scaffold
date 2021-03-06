import bc.*;

import java.text.Collator;
import java.util.*;
import java.io.*;

/************************************************************************************************
 * How Team Array Is Stored:
 *              EARTH
 * 0 - Number of Rockets launched to Mars
 * 1 - Number of Workers launched to Mars
 * 2 - Number of Rangers launched to Mars
 * 3 - Number of Mages   launched to Mars
 * 4 - Number of Healers launched to Mars
 * 5 - Number of Knights launched to Mars
 *
 *              MARS
 * 0 - Number of Rockets added
 * 1 - Number of Workers added
 * 2 - Number of Rangers added
 * 3 - Number of Mages   added
 * 4 - Number of Healers added
 * 5 - Number of Knights added
 *
 */

public class Player {
    public static GameController gc = new GameController();
    public static Team myTeam = gc.team();
    public static Direction[] directions = Direction.values();
    public static Cell GridEarth[][];
    public static Cell GridMars[][];
    public static int[] unitCounts = new int[7];//Rockets Factories Workers Rangers Mages Healers Knights
    public static VecUnit units, initialUnits;
    public static int maxWorkerAmount, unitsPerRocket = 2, unitsPerFactory = 2;
    public static ArrayList<Rocket> rockets = new ArrayList<>();
    public static ArrayList<Factory> factories = new ArrayList<>();
    public static ArrayList<Worker> workers = new ArrayList<>();
    public static ArrayList<Ranger> rangers = new ArrayList<>();
    public static ArrayList<Mage> mages = new ArrayList<>();
    public static ArrayList<Healer> healers = new ArrayList<>();
    public static ArrayList<Knight> knights = new ArrayList<>();
    public static Unit unit;
    public static Veci32 communications;//TEAM ARRAY to comm. between Earth and Mars
    public static PlanetMap EarthMap = gc.startingMap(Planet.Earth);
    public static PlanetMap MarsMap = gc.startingMap(Planet.Mars);

    public static void main(String[] args) {
        units = gc.myUnits();
        if (gc.planet() == Planet.Earth) {
            gc.queueResearch(UnitType.Rocket);
            initialUnits = EarthMap.getInitial_units();
            GridEarth = new Cell[(int) EarthMap.getHeight()][(int) EarthMap.getWidth()];
            GridMars = new Cell[(int) MarsMap.getHeight()][(int) MarsMap.getWidth()];
            for (int i = 0; i < units.size(); i++) {
                workers.add(new Worker(units.get(i)));
            }
        }
        constructGrid();
        while (true) {
            try {
                units = gc.myUnits();
                communications = gc.getTeamArray((gc.planet() == Planet.Earth) ? Planet.Mars : Planet.Earth);//Set comms to teamArray of other planets
                if (gc.planet() == Planet.Mars) checkArrayLists(units);
                maxWorkerAmount = (rockets.size() * unitsPerRocket) + (factories.size() * unitsPerFactory);
                System.out.println("ROUND: " + gc.round() + "\tKARBONITE: " + gc.karbonite() + "\tTIME: " + gc.getTimeLeftMs());//getTimeLeftMs() has yet to be added to the battlecode.jar, just ignore it for now
                for (int i = 0; i < units.size(); i++) {
                    unit = units.get(i);
                    if (unit.location().isInGarrison()) {//Skip over these units
                        switch (unit.unitType()){
                            case Worker:
                                unitCounts[2]++;
                                break;
                            case Ranger:
                                unitCounts[3]++;
                                break;
                            case Mage:
                                unitCounts[4]++;
                                break;
                            case Healer:
                                unitCounts[5]++;
                                break;
                            case Knight:
                                unitCounts[6]++;
                                break;
                        }
                        continue;

                    }
                    switch (unit.unitType()) {
                        case Rocket:
                            if (gc.planet() == Planet.Earth) {
                                for (int j = 0; j < rockets.size(); j++) {
                                    if (rockets.get(j).unit.id() == unit.id()) {
                                        if (unit.structureIsBuilt() == 1) {
                                            rockets.get(j).runEarth();
                                        }
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            } else {
                                for (int j = 0; j < rockets.size(); j++) {
                                    if (rockets.get(j).unit.id() == unit.id()) {
                                        rockets.get(j).runMars();
                                        break;
                                    }
                                }
                                GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            }
                            unitCounts[0]++;
                            break;
                        case Factory:
                            for (int j = 0; j < factories.size(); j++) {
                                if (factories.get(j).unit.id() == unit.id()) {
                                    if (unit.structureIsBuilt() == 1) {
                                        factories.get(j).runEarth();
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            }
                            unitCounts[1]++;
                            break;
                        case Worker: //We don't need UnitType.Worker since it is an enum
                            if (gc.planet() == Planet.Earth) {
                                for (int j = 0; j < workers.size(); j++) {
                                    if (workers.get(j).unit.id() == unit.id()) {
                                        workers.get(j).runEarth();
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            } else {
                                for (int j = 0; j < workers.size(); j++) {
                                    if (workers.get(j).unit.id() == unit.id()) {
                                        workers.get(j).runMars();
                                        break;
                                    }
                                }
                                GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            }
                            unitCounts[2]++;
                            break;
                        case Ranger: //We don't need UnitType.Worker since it is an enum
                            if (gc.planet() == Planet.Earth) {
                                for (int j = 0; j < rangers.size(); j++) {
                                    if (rangers.get(j).unit.id() == unit.id()) {
                                        rangers.get(j).runEarth();
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            } else {
                                for (int j = 0; j < rangers.size(); j++) {
                                    if (rangers.get(j).unit.id() == unit.id()) {
                                        rangers.get(j).runMars();
                                        break;
                                    }
                                }
                                GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            }
                            unitCounts[3]++;
                            break;
                        case Mage: //We don't need UnitType.Worker since it is an enum
                            if (gc.planet() == Planet.Earth) {
                                for (int j = 0; j < mages.size(); j++) {
                                    if (mages.get(j).unit.id() == unit.id()) {
                                        mages.get(j).runEarth();
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            } else {
                                for (int j = 0; j < mages.size(); j++) {
                                    if (mages.get(j).unit.id() == unit.id()) {
                                        mages.get(j).runMars();
                                        break;
                                    }
                                }
                                GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            }
                            unitCounts[4]++;
                            break;
                        case Healer: //We don't need UnitType.Worker since it is an enum
                            if (gc.planet() == Planet.Earth) {
                                for (int j = 0; j < healers.size(); j++) {
                                    if (healers.get(j).unit.id() == unit.id()) {
                                        healers.get(j).runEarth();
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            } else {
                                for (int j = 0; j < healers.size(); j++) {
                                    if (healers.get(j).unit.id() == unit.id()) {
                                        healers.get(j).runMars();
                                        break;
                                    }
                                }
                                GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            }
                            unitCounts[5]++;
                            break;
                        case Knight: //We don't need UnitType.Worker since it is an enum
                            if (gc.planet() == Planet.Earth) {
                                for (int j = 0; j < knights.size(); j++) {
                                    if (knights.get(j).unit.id() == unit.id()) {
                                        knights.get(j).runEarth();
                                        break;
                                    }
                                }
                                GridEarth[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            } else {
                                for (int j = 0; j < knights.size(); j++) {
                                    if (knights.get(j).unit.id() == unit.id()) {
                                        knights.get(j).runMars();
                                        break;
                                    }
                                }
                                GridMars[unit.location().mapLocation().getY()][unit.location().mapLocation().getX()].setValue("--");
                            }
                            unitCounts[6]++;
                            break;
                    }
                    units = gc.myUnits();
                }
                //removeUnits();
                /*for (int i = 0; i < workers.size(); i++) {
                    for(int j = 0; j < gc.myUnits().size(); j++){
                        if(workers.get(i).unit.id()==gc.myUnits().get(j).id()){
                            unit = gc.myUnits().get(j);
                            break;
                        }
                    }
                    if (unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    workers.get(i).run();
                }
                for (int i = 0; i < rangers.size(); i++) {
                    unit = rangers.get(i).unit;
                    if (unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    rangers.get(i).run();
                }
                for (int i = 0; i < mages.size(); i++) {
                    unit = mages.get(i).unit;
                    if (unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    mages.get(i).run();
                }
                for (int i = 0; i < healers.size(); i++) {
                    unit = healers.get(i).unit;
                    if (unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    healers.get(i).run();
                }
                for (int i = 0; i < knights.size(); i++) {
                    unit = knights.get(i).unit;
                    if (unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    knights.get(i).run();
                }
                for (int i = 0; i < factories.size(); i++) {
                    unit = factories.get(i).unit;
                    if (unit.structureIsBuilt() == 0 || unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    factories.get(i).run();
                }
                for (int i = 0; i < rockets.size(); i++) {
                    for(int j = 0; j < gc.myUnits().size(); j++){
                        if(rockets.get(i).unit.id()==gc.myUnits().get(j).id()){
                            unit = gc.myUnits().get(j);
                            break;
                        }
                    }
                    if (unit.structureIsBuilt() == 0 || unit.location().isInSpace() || unit.location().isInGarrison()) {
                        continue;
                    }
                    rockets.get(i).run();
                }*/

            } catch (Exception e) {
            }
            gc.nextTurn();
        }
    }

    public static void removeUnits() {
        boolean found;

        for (int i = 0; i < (unitCounts[0] - rockets.size()); i++) {
            for (int j = 0; j < rockets.size(); j++) {
                found = false;
                for (int k = 0; k < units.size(); k++) {
                    if (units.get(i).unitType() == UnitType.Rocket) {
                        if (rockets.get(j).unit.id() == units.get(k).id()) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    if(gc.planet()==Planet.Earth){
                        GridEarth[rockets.get(j).unit.location().mapLocation().getY()][rockets.get(j).unit.location().mapLocation().getX()].setValue(null);
                    } else {
                        GridMars[rockets.get(j).unit.location().mapLocation().getY()][rockets.get(j).unit.location().mapLocation().getX()].setValue(null);
                    }
                    rockets.remove(j);
                }
            }
        }

        for (int i = 0; i < (unitCounts[1] - factories.size()); i++) {
            for (int j = 0; j < factories.size(); j++) {
                found = false;
                for (int k = 0; k < units.size(); k++) {
                    if (units.get(i).unitType() == UnitType.Factory) {
                        if (factories.get(j).unit.id() == units.get(k).id()) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    if(gc.planet()==Planet.Earth){
                        GridEarth[factories.get(j).unit.location().mapLocation().getY()][factories.get(j).unit.location().mapLocation().getX()].setValue(null);
                    } else {
                        GridMars[factories.get(j).unit.location().mapLocation().getY()][factories.get(j).unit.location().mapLocation().getX()].setValue(null);
                    }
                    factories.remove(j);
                }
            }
        }

        for (int i = 0; i < (unitCounts[2] - workers.size()); i++) {
            for (int j = 0; j < workers.size(); j++) {
                found = false;
                for (int k = 0; k < units.size(); k++) {
                    if (units.get(i).unitType() == UnitType.Worker) {
                        if (workers.get(j).unit.id() == units.get(k).id()) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    if(gc.planet()==Planet.Earth){
                        GridEarth[workers.get(j).unit.location().mapLocation().getY()][workers.get(j).unit.location().mapLocation().getX()].setValue(null);
                    } else {
                        GridMars[workers.get(j).unit.location().mapLocation().getY()][workers.get(j).unit.location().mapLocation().getX()].setValue(null);
                    }
                    workers.remove(j);
                }
            }
        }

        for (int i = 0; i < (unitCounts[3] - rangers.size()); i++) {
            for (int j = 0; j < rangers.size(); j++) {
                found = false;
                for (int k = 0; k < units.size(); k++) {
                    if (units.get(i).unitType() == UnitType.Ranger) {
                        if (rangers.get(j).unit.id() == units.get(k).id()) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    if(gc.planet()==Planet.Earth){
                        GridEarth[rangers.get(j).unit.location().mapLocation().getY()][rangers.get(j).unit.location().mapLocation().getX()].setValue(null);
                    } else {
                        GridMars[rangers.get(j).unit.location().mapLocation().getY()][rangers.get(j).unit.location().mapLocation().getX()].setValue(null);
                    }
                    rangers.remove(j);
                }
            }
        }

        for (int i = 0; i < (unitCounts[4] - mages.size()); i++) {
            for (int j = 0; j < mages.size(); j++) {
                found = false;
                for (int k = 0; k < units.size(); k++) {
                    if (units.get(i).unitType() == UnitType.Mage) {
                        if (mages.get(j).unit.id() == units.get(k).id()) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    if(gc.planet()==Planet.Earth){
                        GridEarth[mages.get(j).unit.location().mapLocation().getY()][mages.get(j).unit.location().mapLocation().getX()].setValue(null);
                    } else {
                        GridMars[mages.get(j).unit.location().mapLocation().getY()][mages.get(j).unit.location().mapLocation().getX()].setValue(null);
                    }
                    mages.remove(j);
                }
            }
        }

        for (int i = 0; i < (unitCounts[5] - healers.size()); i++) {
            for (int j = 0; j < healers.size(); j++) {
                found = false;
                for (int k = 0; k < units.size(); k++) {
                    if (units.get(i).unitType() == UnitType.Healer) {
                        if (healers.get(j).unit.id() == units.get(k).id()) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    if(gc.planet()==Planet.Earth){
                        GridEarth[healers.get(j).unit.location().mapLocation().getY()][healers.get(j).unit.location().mapLocation().getX()].setValue(null);
                    } else {
                        GridMars[healers.get(j).unit.location().mapLocation().getY()][healers.get(j).unit.location().mapLocation().getX()].setValue(null);
                    }
                    healers.remove(j);
                }
            }
        }

        for (int i = 0; i < (unitCounts[6] - knights.size()); i++) {
            for (int j = 0; j < knights.size(); j++) {
                found = false;
                for (int k = 0; k < units.size(); k++) {
                    if (units.get(i).unitType() == UnitType.Knight) {
                        if (knights.get(j).unit.id() == units.get(k).id()) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    if(gc.planet()==Planet.Earth){
                        GridEarth[knights.get(j).unit.location().mapLocation().getY()][knights.get(j).unit.location().mapLocation().getX()].setValue(null);
                    } else {
                        GridMars[knights.get(j).unit.location().mapLocation().getY()][knights.get(j).unit.location().mapLocation().getX()].setValue(null);
                    }
                    knights.remove(j);
                }
            }
        }
    }

    public static void checkArrayLists(VecUnit units) {
        if (communications.get(0) > gc.getTeamArray(Planet.Mars).get(0)) {
            boolean found = false;
            for (int i = 0; i < communications.get(0) - gc.getTeamArray(Planet.Mars).get(0); i++) {
                int startArraySize = rockets.size();
                for (int j = 0; j < units.size(); j++) {
                    for (int k = 0; k < rockets.size(); k++) {
                        if (units.get(j).id() == rockets.get(k).unit.id()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        rockets.add(new Rocket(units.get(j)));
                    }
                }
                if (startArraySize == rockets.size()) {
                    break;
                }
            }
        }
        if (communications.get(1) > gc.getTeamArray(Planet.Mars).get(1)) {
            boolean found = false;
            for (int i = 0; i < communications.get(0) - gc.getTeamArray(Planet.Mars).get(0); i++) {
                int startArraySize = workers.size();
                for (int j = 0; j < units.size(); j++) {
                    for (int k = 0; k < workers.size(); k++) {
                        if (units.get(j).id() == workers.get(k).unit.id()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        workers.add(new Worker(units.get(j)));
                    }
                }
                if (startArraySize == workers.size()) {
                    break;
                }
            }
        }
        if (communications.get(2) > gc.getTeamArray(Planet.Mars).get(2)) {
            boolean found = false;
            for (int i = 0; i < communications.get(0) - gc.getTeamArray(Planet.Mars).get(0); i++) {
                int startArraySize = rangers.size();
                for (int j = 0; j < units.size(); j++) {
                    for (int k = 0; k < rangers.size(); k++) {
                        if (units.get(j).id() == rangers.get(k).unit.id()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        rangers.add(new Ranger(units.get(j)));
                    }
                }
                if (startArraySize == rangers.size()) {
                    break;
                }
            }
        }
        if (communications.get(3) > gc.getTeamArray(Planet.Mars).get(3)) {
            boolean found = false;
            for (int i = 0; i < communications.get(0) - gc.getTeamArray(Planet.Mars).get(0); i++) {
                int startArraySize = mages.size();
                for (int j = 0; j < units.size(); j++) {
                    for (int k = 0; k < mages.size(); k++) {
                        if (units.get(j).id() == mages.get(k).unit.id()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        mages.add(new Mage(units.get(j)));
                    }
                }
                if (startArraySize == mages.size()) {
                    break;
                }
            }
        }
        if (communications.get(4) > gc.getTeamArray(Planet.Mars).get(4)) {
            boolean found = false;
            for (int i = 0; i < communications.get(0) - gc.getTeamArray(Planet.Mars).get(0); i++) {
                int startArraySize = healers.size();
                for (int j = 0; j < units.size(); j++) {
                    for (int k = 0; k < healers.size(); k++) {
                        if (units.get(j).id() == healers.get(k).unit.id()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        healers.add(new Healer(units.get(j)));
                    }
                }
                if (startArraySize == healers.size()) {
                    break;
                }
            }
        }
        if (communications.get(5) > gc.getTeamArray(Planet.Mars).get(5)) {
            boolean found = false;
            for (int i = 0; i < communications.get(0) - gc.getTeamArray(Planet.Mars).get(0); i++) {
                int startArraySize = knights.size();
                for (int j = 0; j < units.size(); j++) {
                    for (int k = 0; k < knights.size(); k++) {
                        if (units.get(j).id() == knights.get(k).unit.id()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        knights.add(new Knight(units.get(j)));
                    }
                }
                if (startArraySize == knights.size()) {
                    break;
                }
            }
        }
    }

    public static Unit findUnit(UnitType type) {
        VecUnit nearby = gc.myUnits();//gc.senseNearbyUnitsByTeam(creator.location().mapLocation(), 1, myTeam);
        for (int i = 0; i < nearby.size(); i++) {
            boolean found = false;
            if (nearby.get(i).unitType() == type) {
                switch (type) {
                    case Worker:
                        for (int j = 0; j < Player.workers.size(); j++) {
                            if (Player.workers.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                    case Ranger:
                        for (int j = 0; j < Player.rangers.size(); j++) {
                            if (Player.rangers.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                    case Mage:
                        for (int j = 0; j < Player.mages.size(); j++) {
                            if (Player.mages.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                    case Healer:
                        for (int j = 0; j < Player.healers.size(); j++) {
                            if (Player.healers.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                    case Knight:
                        for (int j = 0; j < Player.knights.size(); j++) {
                            if (Player.knights.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                    case Factory:
                        for (int j = 0; j < Player.factories.size(); j++) {
                            if (Player.factories.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                    case Rocket:
                        for (int j = 0; j < Player.rockets.size(); j++) {
                            if (Player.rockets.get(j).unit.id() == nearby.get(i).id()) {
                                found = true;
                                break;
                            }
                        }
                        break;
                }
                if (!found) {
                    return nearby.get(i);
                }
            }
        }
        return null;//Shouldn't ever reach here
    }

    public static void constructGrid() {
        try {
            MapLocation tempLocationEarth = new MapLocation(Planet.Earth, 0, 0);
            MapLocation tempLocationMars = new MapLocation(Planet.Mars, 0, 0);
            for (int y = 0; y < GridEarth[0].length; y++) {
                for (int x = 0; x < GridEarth[1].length; x++) {
                    tempLocationEarth.setX(x);
                    tempLocationEarth.setY(y);
                    Cell c;
                    if (EarthMap.isPassableTerrainAt(tempLocationEarth) != 0 && !gc.hasUnitAtLocation(tempLocationEarth)) {
                        c = new Cell(x, y, true, null);
                        GridEarth[y][x] = c;
                        GridEarth[y][x].setKarbonite((int) EarthMap.initialKarboniteAt(tempLocationEarth));
                    } else {
                        c = new Cell(x, y, false, "--");
                        GridEarth[y][x] = c;
                        GridEarth[y][x].setKarbonite((int) EarthMap.initialKarboniteAt(tempLocationEarth));
                    }
                }
            }
            for (int y = 0; y < GridMars[0].length; y++) {
                for (int x = 0; x < GridMars[1].length; x++) {
                    tempLocationMars.setX(x);
                    tempLocationMars.setY(y);
                    Cell c;
                    if (MarsMap.isPassableTerrainAt(tempLocationMars) != 0 && !gc.hasUnitAtLocation(tempLocationMars)) {
                        c = new Cell(x, y, true, null);
                        GridMars[y][x] = c;
                    } else {
                        c = new Cell(x, y, false, "--");
                        GridMars[y][x] = c;
                    }
                }
            }
            for (int i = 0; i < initialUnits.size(); i++) {
                Unit unit = initialUnits.get(i);
                int X = unit.location().mapLocation().getX(), Y = unit.location().mapLocation().getY();
                if (unit.team() == myTeam) {
                    GridEarth[Y][X].setValue("--");
                } else {
                    GridEarth[Y][X].setValue("--");
                }
            }
        } catch (Exception e) {

        }
    }
}


