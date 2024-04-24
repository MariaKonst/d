import java.io.*;
import java.util.*;

class Apartment {
    String city;
    int rooms;
    double area;
    double price;
    String phone;

    public Apartment(String city, int rooms, double area, double price, String phone) {
        this.city = city;
        this.rooms = rooms;
        this.area = area;
        this.price = price;
        this.phone = phone;
    }
}

class UnsuitableApartmentsException extends Exception {
    public UnsuitableApartmentsException(String message) {
        super(message);
    }
}

public class Main {
    public static void main(String[] args) {
        String inputFile = "testData_Apartments(1).txt";
        String outputFile = "broker_phones.txt";

        try {
            List<Apartment> apartments = readApartments(inputFile);
            List<String> brokerPhones = getBrokerPhones(apartments);
            writeBrokerPhones(brokerPhones, outputFile);

            String mostApartmentsCity = getCityWithMostApartments(apartments);
            System.out.println("Градове с най-много апартаменти за продажба: " + mostApartmentsCity);
        } catch (IOException | UnsuitableApartmentsException e) {
            e.printStackTrace();
        }
    }

    private static List<Apartment> readApartments(String inputFile) throws IOException {
        List<Apartment> apartments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String city = parts[0].trim();
                int rooms = Integer.parseInt(parts[1].trim());
                double area = Double.parseDouble(parts[2].trim());
                double price = Double.parseDouble(parts[3].trim());
                String phone = parts[4].trim();
                apartments.add(new Apartment(city, rooms, area, price, phone));
            }
        }
        return apartments;
    }

    private static List<String> getBrokerPhones(List<Apartment> apartments) throws UnsuitableApartmentsException {
        List<Apartment> suitableApartments = new ArrayList<>();
        for (Apartment apartment : apartments) {
            if (apartment.rooms == 3 && apartment.area > 100 &&
                    (apartment.city.equals("София") || apartment.city.equals("Бургас") || apartment.city.equals("Варна"))) {
                suitableApartments.add(apartment);
            }
        }
        if (suitableApartments.isEmpty()) {
            throw new UnsuitableApartmentsException("Няма подходящи апартаменти за продажба.");
        }
        suitableApartments.sort(Comparator.comparingDouble(apartment -> apartment.price));
        List<String> brokerPhones = new ArrayList<>();
        Set<String> seenPhones = new HashSet<>();
        for (int i = 0; i < Math.min(5, suitableApartments.size()); i++) {
            String phone = suitableApartments.get(i).phone;
            if (!seenPhones.contains(phone)) {
                brokerPhones.add(phone);
                seenPhones.add(phone);
            }
        }
        return brokerPhones;
    }

    private static void writeBrokerPhones(List<String> brokerPhones, String outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String phone : brokerPhones) {
                writer.write(phone + "\n");
            }
        }
    }

    private static String getCityWithMostApartments(List<Apartment> apartments) {
        Map<String, Integer> cityCounts = new HashMap<>();
        for (Apartment apartment : apartments) {
            cityCounts.put(apartment.city, cityCounts.getOrDefault(apartment.city, 0) + 1);
        }
        int maxCount = 0;
        String mostApartmentsCity = "";
        for (Map.Entry<String, Integer> entry : cityCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostApartmentsCity = entry.getKey();
            }
        }
        return mostApartmentsCity;
    }
}
