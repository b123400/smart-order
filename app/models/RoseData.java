/*
Copyright (c) 2016i, ywesee GmbH, created by ML <cybrmx@gmail.com>

This file is part of AmikoRose.

AmikoRose is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxl.java.shared.NotaPosition;
import com.maxl.java.shared.User;
import com.maxl.java.shared.NewUser;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by maxl on 26.06.2016.
 * Singleton Pattern
 */
public final class RoseData {

    private static volatile RoseData instance;

    private HashMap<String, User> rose_user_map;
    private HashMap<String, NewUser> rose_new_user_map;
    private HashMap<String, Float> rose_sales_figs_map;
    private HashMap<String, String> rose_ids_map;
    private HashMap<String, String> rose_direct_subst_map;
    private ArrayList<String> rose_autogenerika_list;
    private ArrayList<String> rose_auth_keys_list;
    private HashMap<String, List<NotaPosition>> rose_nota_map;
    private HashMap<String, Pair<Integer, Integer>> rose_stock_map;

    private RoseData() {
        loadAllFiles();
    }

    /**
     * Get the only instance of this class. Singleton pattern.
     *
     * @return
     */
    public static RoseData getInstance() {
        if (instance == null) {
            synchronized (RoseData.class) {
                if (instance == null) {
                    instance = new RoseData();
                }
            }
        }
        return instance;
    }

    public HashMap<String, User> user_map() {
        return this.rose_user_map;
    }

    public HashMap<String, NewUser> new_user_map() {
        return this.rose_new_user_map;
    }

    public HashMap<String, Float> sales_figs_map() {
        return this.rose_sales_figs_map;
    }

    public HashMap<String, String> rose_ids_map() {
        return this.rose_ids_map;
    }

    public HashMap<String, String> rose_direct_subst_map() {
        return this.rose_direct_subst_map;
    }

    public ArrayList<String> autogenerika_list() {
        return this.rose_autogenerika_list;
    }

    public ArrayList<String> auth_keys_list() {
        return this.rose_auth_keys_list;
    }

    public HashMap<String, Pair<Integer, Integer>> rose_stock_map() {
        return this.rose_stock_map;
    }

    public HashMap<String, List<NotaPosition>> rose_nota_map() {
        return this.rose_nota_map;
    }

    public void loadFile(String file_name) {
        String rose_path = System.getProperty("user.dir") + Constants.ROSE_DIR;

        System.out.print("# Re-loading " + file_name + "... ");

        if (file_name.equals("rose_conditions.ser.clear"))
            rose_user_map = loadRoseUserMap(rose_path + file_name);
        else if (file_name.equals("rose_conditions_new.json"))
            rose_new_user_map = loadRoseNewUserMap(rose_path + file_name);
        else if (file_name.equals("rose_sales_fig.ser.clear"))
            rose_sales_figs_map = loadRoseSalesFigures(rose_path + file_name);
        else if (file_name.equals("rose_ids.ser.clear"))
            rose_ids_map = loadRoseIds(rose_path + file_name);
        else if (file_name.equals("rose_direct_subst.ser.clear"))
            rose_direct_subst_map = loadRoseDirectSubst(rose_path + file_name);
        else if (file_name.equals("rose_autogenerika.ser.clear"))
            rose_autogenerika_list = loadRoseAutoGenerika(rose_path + file_name);
        else if (file_name.equals("rose_nota.ser.clear"))
            rose_nota_map = loadRoseNotaMap(rose_path + file_name);
        else if (file_name.equals("rose_auth_keys.txt"))
            rose_auth_keys_list = loadRoseAuthKeys(rose_path + file_name);
        else if (file_name.equals("rose_stock.csv"))
            rose_stock_map = loadRoseStockMaps(rose_path + file_name);
        else if (file_name.equals("rose_settings.json"))
            loadRoseSettingsFile(rose_path + file_name);

        System.out.println("OK");
    }

    public void loadAllFiles() {
        String rose_path = System.getProperty("user.dir") + Constants.ROSE_DIR;

        // System.out.print("\n# Loading rose_conditions_ser.clear... ");
        rose_user_map = loadRoseUserMap(rose_path + "rose_conditions.ser.clear");
        // System.out.println("OK");

        rose_new_user_map = loadRoseNewUserMap(rose_path + "rose_conditions_new.json");

        // System.out.print("# Loading rose_sales_fig.ser.clear... ");
        rose_sales_figs_map = loadRoseSalesFigures(rose_path + "rose_sales_fig.ser.clear");
        // System.out.println("OK");

        // System.out.print("# Loading rose_ids.ser.clear... ");
        rose_ids_map = loadRoseIds(rose_path + "rose_ids.ser.clear");
        // System.out.println("OK");

        // System.out.print("# Loading rose_direct_subst.ser.clear... ");
        rose_direct_subst_map = loadRoseDirectSubst(rose_path + "rose_direct_subst.ser.clear");
        // System.out.println("OK");

        // System.out.print("# Loading rose_autogenerika.ser.clear... ");
        rose_autogenerika_list = loadRoseAutoGenerika(rose_path + "rose_autogenerika.ser.clear");
        // System.out.println("OK");

        // System.out.print("# Loading rose_nota.ser.clear... ");
        rose_nota_map = loadRoseNotaMap(rose_path + "rose_nota.ser.clear");
        // System.out.println("OK");

        // System.out.print("# Loading rose_auth_keys.txt... ");
        rose_auth_keys_list = loadRoseAuthKeys(rose_path + "rose_auth_keys.txt");
        // System.out.println("OK");

        // System.out.print("# Loading rose_stock.csv... ");
        rose_stock_map = loadRoseStockMaps(rose_path + "rose_stock.csv");
        // System.out.println("OK");

        loadRoseSettingsFile(rose_path + "rose_settings.json");
    }

    private ArrayList<String> loadRoseAuthKeys(String file_name) {
        return FileOps.readFromTxtToList(file_name);
    }

    /**
     * Loads Rose users
     * Format: gln code -> user
     *
     * @param ser_file_name
     * @return user map
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, User> loadRoseUserMap(String ser_file_name) {
        HashMap<String, User> user_map = new HashMap<>();

        String json_file_path = ser_file_name.replace(".ser.clear", ".json");
        File json_file = new File(json_file_path);
        boolean json_success = false;
        if (json_file.exists()) {
            System.out.println("Using json file instead of .ser.clear: " + json_file_path);
            ObjectMapper mapper = new ObjectMapper();
            try {
                user_map = mapper.readValue(json_file,
                    new TypeReference<HashMap<String, User>>() { } );
                json_success = true;
            } catch (Exception e) {
                System.out.println("Exception in loadRoseUserMap: " + e.getMessage());
            }
        }

        if (!json_success) {
            System.out.println("JSON file not found or not success, fallback to ser file: " + json_file_path);
            byte[] serialized_object = FileOps.readBytesFromFile(ser_file_name);
            if (serialized_object != null) {
                user_map = (HashMap<String, User>) FileOps.deserialize(serialized_object);
            }
        }
        return user_map;
    }

    private HashMap<String, NewUser> loadRoseNewUserMap(String file_name) {
        HashMap<String, NewUser> new_user_map = new HashMap<>();
        File json_file = new File(file_name);

        ObjectMapper mapper = new ObjectMapper();
        try {
            new_user_map = mapper.readValue(json_file,
                new TypeReference<HashMap<String, NewUser>>() { } );
        } catch (Exception e) {
            System.out.println("Exception in loadRoseNewUserMap: " + e.getMessage());
        }

        return new_user_map;
    }

    /**
     * Loads Rose sales figures
     * Format: pharma code -> sales figure
     *
     * @param ser_file_name
     * @return sales figures map
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, Float> loadRoseSalesFigures(String ser_file_name) {
        HashMap<String, Float> sales_figures_map = new HashMap<>();
        byte[] serialized_object = FileOps.readBytesFromFile(ser_file_name);
        if (serialized_object != null) {
            sales_figures_map = (HashMap<String, Float>) FileOps.deserialize(serialized_object);
        }
        return sales_figures_map;
    }

    /**
     * Loads Rose ids
     * Format: gln_code -> rose id
     *
     * @param: ser_file_name
     * @return: rose ids map
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, String> loadRoseIds(String ser_file_name) {
        HashMap<String, String> rose_ids_map = new HashMap<>();

        String json_file_path = ser_file_name.replace(".ser.clear", ".json");
        File json_file = new File(json_file_path);
        boolean json_success = false;
        if (json_file.exists()) {
            System.out.println("Using json file instead of .ser.clear: " + json_file_path);
            ObjectMapper mapper = new ObjectMapper();
            try {
                rose_ids_map = mapper.readValue(json_file,
                    new TypeReference<HashMap<String, String>>() { } );
                json_success = true;
            } catch (Exception e) {
                System.out.println("Exception in loadRoseIds: " + e.getMessage());
            }
        }

        if (!json_success) {
            System.out.println("JSON file not found or not success, fallback to ser file: " + json_file_path);
            byte[] serialized_object = FileOps.readBytesFromFile(ser_file_name);
            if (serialized_object != null) {
                rose_ids_map = (HashMap<String, String>) FileOps.deserialize(serialized_object);
            }
        }
        return rose_ids_map;
    }

    /**
     * Loads Rose direct substitutions map
     * Format: from -> to
     *
     * @param: ser_file_name
     * @return: rose ids map
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, String> loadRoseDirectSubst(String ser_file_name) {
        HashMap<String, String> rose_direct_subst_map = new HashMap<>();

        String json_file_path = ser_file_name.replace(".ser.clear", ".json");
        File json_file = new File(json_file_path);
        boolean json_success = false;
        if (json_file.exists()) {
            System.out.println("Using json file instead of .ser.clear: " + json_file_path);
            ObjectMapper mapper = new ObjectMapper();
            try {
                rose_direct_subst_map = mapper.readValue(json_file,
                    new TypeReference<HashMap<String, String>>() { } );
                json_success = true;
            } catch (Exception e) {
                System.out.println("Exception in loadRoseDirectSubst: " + e.getMessage());
            }
        }

        if (!json_success) {
            System.out.println("JSON file not found or not success, fallback to ser file: " + json_file_path);
            byte[] serialized_object = FileOps.readBytesFromFile(ser_file_name);
            if (serialized_object != null) {
                rose_direct_subst_map = (HashMap<String, String>) FileOps.deserialize(serialized_object);
            }
        }

        return rose_direct_subst_map;
    }

    /**
     * Loads Rose list of autogenerika
     *
     * @param ser_file_name
     * @return
     */
    @SuppressWarnings("unchecked")
    private ArrayList<String> loadRoseAutoGenerika(String ser_file_name) {
        ArrayList<String> auto_generika_list = new ArrayList<>();

        String json_file_path = ser_file_name.replace(".ser.clear", ".json");
        File json_file = new File(json_file_path);
        boolean json_success = false;
        if (json_file.exists()) {
            System.out.println("Using json file instead of .ser.clear: " + json_file_path);
            ObjectMapper mapper = new ObjectMapper();
            try {
                auto_generika_list = mapper.readValue(json_file,
                    new TypeReference<ArrayList<String>>() { } );
                json_success = true;
            } catch (Exception e) {
                System.out.println("Exception in loadRoseAutoGenerika: " + e.getMessage());
            }
        }

        if (!json_success) {
            System.out.println("JSON file not found or not success, fallback to ser file: " + json_file_path);
            byte[] serialized_object = FileOps.readBytesFromFile(ser_file_name);
            if (serialized_object != null) {
                auto_generika_list = (ArrayList<String>) FileOps.deserialize(serialized_object);
            }
        }

        return auto_generika_list;
    }

    /**
     * Loads Rose nota
     * Format: customer nr -> list of nota positions
     *
     * @param ser_file_name
     * @return nota map
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, List<NotaPosition>> loadRoseNotaMap(String ser_file_name) {
        HashMap<String, List<NotaPosition>> nota_map = new HashMap<>();

        String json_file_path = ser_file_name.replace(".ser.clear", ".json");
        File json_file = new File(json_file_path);
        boolean json_success = false;
        if (json_file.exists()) {
            System.out.println("Using json file instead of .ser.clear: " + json_file_path);
            ObjectMapper mapper = new ObjectMapper();
            try {
                nota_map = mapper.readValue(json_file,
                    new TypeReference<HashMap<String, List<NotaPosition>>>() { } );
                json_success = true;
            } catch (Exception e) {
                System.out.println("Exception in loadRoseNotaMap: " + e.getMessage());
            }
        }

        if (!json_success) {
            System.out.println("JSON file not found or not success, fallback to ser file: " + json_file_path);
            byte[] serialized_object = FileOps.readBytesFromFile(ser_file_name);
            if (serialized_object != null) {
                nota_map = (HashMap<String, List<NotaPosition>>) FileOps.deserialize(serialized_object);
            }
        }

        return nota_map;
    }

    /**
     * Loads Rose stock maps (rose + voigt)
     *
     * @param file_name
     * @return
     */
    private HashMap<String, Pair<Integer, Integer>> loadRoseStockMaps(String file_name) {
        HashMap<String, Pair<Integer, Integer>> stock_map = new HashMap<>();
        try {
            File file = new File(file_name);
            if (!file.exists())
                return null;

            FileInputStream fis = new FileInputStream(file_name);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                String token[] = line.split(";", -1);
                if (token.length > 2) {
                    String gln = token[0];
                    int stock_rose = Integer.parseInt(token[1]);
                    int stock_voigt = Integer.parseInt(token[2]);
                    stock_map.put(gln, new Pair<>(stock_rose, stock_voigt));
                }
            }
        } catch (Exception e) {
            System.err.println(">> Error in reading csv file: " + file_name);
        }
        return stock_map;
    }

    /**
     * Loads Rose settings JSON file
     */
    @SuppressWarnings("unchecked")
    public void loadRoseSettingsFile(String json_file_name) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};

            File json_file = Paths.get(json_file_name).toFile();
            if (!json_file.exists())
                System.out.println("ERROR: Could not read file " + json_file);

            Map<String, Object> rose_settings = mapper.readValue(json_file, typeRef);

            Constants.doctorPreferences = (LinkedHashMap<String, Integer>) rose_settings.get("doctor_preferences");
            Constants.rosePreferences = (LinkedHashMap<String, Integer>) rose_settings.get("rose_preferences");
       } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a smartorder conform shopping basket for a spefic gln code
     * @param gln_code
     * @return
     */
    public String rose_nota_basket(String gln_code) {
        String nota_basket = "";
        if (rose_nota_map.containsKey(gln_code)) {
            List<NotaPosition> list_of_nota_positions = rose_nota_map.get(gln_code);
            for (NotaPosition p : list_of_nota_positions) {
                nota_basket += "(" + p.pharma_code + "," + p.quantity + ")";
            }
        }
        return nota_basket;
    }

    /**
     * Retrieves status for nota position given a gln code a pharma code and a language
     * @param gln_code
     * @param pharma_code
     * @param lang (de, fr)
     * @return
     */
    public String rose_nota_status(String gln_code, String pharma_code, String lang) {
        if (rose_nota_map.containsKey(gln_code)) {
            List<NotaPosition> list_of_nota_positions = rose_nota_map.get(gln_code);
            for (NotaPosition p : list_of_nota_positions) {
                if (p.pharma_code.equals(pharma_code)) {
                    if (p.status.equals("10")) {
                        if (lang.equals("de"))
                            return "Fehlt auf unbestimmte Zeit beim Hersteller";
                        else if (lang.equals("fr"))
                            return "Manque pour une durée indéterminée chez le fabricant";
                    } else if (p.status.equals("20")) {
                        if (lang.equals("de"))
                            return "Ausstand bis: " + p.delivery_date;
                        else if (lang.equals("fr"))
                            return "Quantité due jusqu’à: " + p.delivery_date;
                    } else if (p.status.equals("30")) {
                        if (lang.equals("de"))
                            return "Beim Lieferanten bestellt";
                        else if (lang.equals("fr"))
                            return "Commandé au fournisseur";
                    }
                }
            }
        }
        return "";
    }

    public String rose_last_order_date(String gln_code, String pharma_code) {
        if (rose_nota_map.containsKey(gln_code)) {
            List<NotaPosition> list_of_nota_positions = rose_nota_map.get(gln_code);
            for (NotaPosition p : list_of_nota_positions) {
                if (p.pharma_code.equals(pharma_code))
                    return p.last_order_date;
            }
        }
        return "";
    }
}
