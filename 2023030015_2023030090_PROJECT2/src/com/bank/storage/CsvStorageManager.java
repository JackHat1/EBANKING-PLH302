package com.bank.storage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvStorageManager implements StorageManager{


    @Override
    public void load(Storable s, String filePath){
        try(BufferedReader reader= new BufferedReader(new FileReader(filePath))){
            String line= reader.readLine();
            if(line!= null){
                s.unmarshal(line);
            }
        }catch(IOException e){
            System.out.println("Load failed: "+ filePath);
        }

    }

    @Override
    public void save(Storable s, String filePath, boolean append){

        try(BufferedWriter writer= new BufferedWriter(new FileWriter(filePath))){
            writer.write(s.marshal());
            writer.newLine();
        }catch(IOException e){
                System.out.println("Save failed: "+ filePath);
        }

    }

    public void saveAll(List<? extends Storable> info, String filePath, boolean append) {
    
        try(BufferedWriter writer= new BufferedWriter(new FileWriter(filePath, append))){

            for(Storable s: info){
                writer.write(s.marshal());
                writer.newLine();
            }
        }catch(IOException e){
            System.out.println("Error: "+ e.getMessage());
        }

    }

        public void saveLines(List<String> lines, String filePath, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, !append))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save lines to: " + filePath);
        }
    }



    public List<String> loadLines(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine())!= null) {
                lines.add(line);
            }
        } catch (IOException e) {
        System.out.println("Load failed: " + filePath);
        }
        return lines;
    }



}
