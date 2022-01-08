package contacts;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static class Contacts {
        private final ArrayList<Contact> contacts;
        private boolean filling = true;
        Scanner scanner = new Scanner(System.in);

        Contacts() {
            contacts = new ArrayList<>(10);
            while (filling) {
                System.out.println("[menu] Enter action (add, remove, edit, count, info, exit):");
                switch (scanner.nextLine()) {
                    case "add":
                        add();
                        break;
                    case "remove":
                        remove();
                        break;
                    case "count":
                        count();
                        break;
                    case "exit":
                        exit();
                        break;
                    case "search":
                        search();
                        break;
                    case "list":
                        list();
                        break;
                    default:
                        System.out.println("No such action");
                }
                System.out.println();
            }
        }

        void add() {
            System.out.println("Enter the type (person, organization):");
            String type = scanner.nextLine();
            switch (type) {
                case "person":
                    contacts.add(new Person());
                    break;
                case "organization":
                    contacts.add(new Organization());
                    break;
            }
        }

        void remove() {
            if (contacts.size() == 0) {
                System.out.println("No records to remove!");
                return;
            }
            list();
            System.out.println("Select a record:");
            int index = scanner.nextInt();
            contacts.remove(index - 1);
            System.out.println("The record removed!");
        }

        void count() {
            System.out.println("The Phone Book has " + contacts.size() + " records.");
        }

        void search() {
            System.out.println("Enter search query:");
            Pattern query = Pattern.compile(scanner.nextLine(), Pattern.CASE_INSENSITIVE);
            int i = 0;
            ArrayList<Contact> contactArrayList = new ArrayList<>();

            for (Contact contact : contacts
            ) {
                String bigString = " ";
                Field[] fields = contact.returnFields();
                for (Field field : fields
                ) {
                    bigString = bigString.concat(contact.getField(field.getName()));
                    //System.out.println(bigString + "   " + field.getName() + "     " + contact.getField(field.getName()));
                }
                Matcher matcher = query.matcher(bigString);
                if (matcher.find()) {
                    contactArrayList.add(contact);
                    i++;
                }
            }
            System.out.println("Found " + i + " results:");
            for (int k = 1; k <= i; k++) {
                if (Objects.equals(contactArrayList.get(k - 1).getClassName(), "contacts.Main$Person"))
                    System.out.println(k + ". " + contactArrayList.get(k - 1).getName() + " " + contactArrayList.get(k - 1).getField("surname"));
                else
                    System.out.println(k + ". " + contactArrayList.get(k - 1).getName());
            }
            System.out.println("[search] Enter action ([number], back, again):");
            String action = scanner.nextLine();
            if (Objects.equals(action, "back")) {
                return;
            } else if (Objects.equals(action, "again")) {
                search();
            } else {
                try {
                    info(contactArrayList.get(Integer.parseInt(action) - 1));
                    System.out.println();
                    edit(contactArrayList.get(Integer.parseInt(action) - 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        void edit(Contact contact) {
            System.out.println("[record] Enter action (edit, delete, menu):");
            switch (scanner.nextLine()) {
                case "delete":
                    contacts.remove(contact);
                    break;
                case "menu":
                    return;
            }
            Contact thisContact = null;
            for (Contact realContact : contacts
            ) {
                if (realContact.equals(contact)) {
                    thisContact = realContact;
                }
            }
            assert thisContact != null;
            if (Objects.equals(thisContact.getClassName(), "contacts.Main$Person")) {
                System.out.println("Select a field (name, surname, number,birth,gender):");
                Person person = (Person) thisContact;
                String input = scanner.nextLine();
                switch (input) {
                    case "name":
                        System.out.println("Enter name:");
                        person.setName(scanner.nextLine());
                        break;
                    case "surname":
                        System.out.println("Enter surname:");
                        person.setSurname(scanner.nextLine());
                        break;
                    case "number":
                        System.out.println("Enter number:");
                        person.setPhoneNumber(scanner.nextLine());
                        break;
                    case "birth":
                        System.out.println("Enter birth date:");
                        person.setBirth_date(scanner.nextLine());
                        break;
                    case "gender":
                        System.out.println("Enter gender:");
                        person.setGender(scanner.nextLine());
                        break;
                    default:
                        System.out.println("No such field:");
                }
            } else {
                System.out.println("Select a field (organization name, address, number):");
                Organization organization = (Organization) thisContact;
                String input = scanner.nextLine();
                switch (input) {
                    case "organization name":
                        System.out.println("Enter organization name:");
                        organization.setName(scanner.nextLine());
                        break;
                    case "address":
                        System.out.println("Enter address:");
                        organization.setAddress(scanner.nextLine());
                        break;
                    case "number":
                        System.out.println("Enter number:");
                        organization.setPhoneNumber(scanner.nextLine());
                        break;
                    default:
                        System.out.println("No such field");
                }
            }
            thisContact.setTimeUpdated(LocalDateTime.now());
            System.out.println("Saved");
            info(thisContact);
            System.out.println();
            edit(thisContact);
        }

        void list() {

            ArrayList<Contact> contactArrayList = new ArrayList<>();
            for (int i = 0; i < contacts.size(); i++) {
                contactArrayList.add(contacts.get(i));
                if (Objects.equals(contacts.get(i).getClassName(), "contacts.Main$Person")) {
                    Person person = (Person) contacts.get(i);
                    System.out.println(i + 1 + ". " + person.getName() + " " + person.getSurname());
                } else {
                    Organization organization = (Organization) contacts.get(i);
                    System.out.println(i + 1 + ". " + organization.getName());
                }
            }
            System.out.println("[list] Enter action ([number], back):");
            String action = scanner.nextLine();
            if (Objects.equals(action, "back")) {
                return;
            } else
                info(contactArrayList.get(Integer.parseInt(action) - 1));
            System.out.println();
            edit(contactArrayList.get(Integer.parseInt(action) - 1));
        }

        void info(Contact contact) {
            if ((Objects.equals(contact.getClassName(), "contacts.Main$Person"))) {
                Person person = (Person) contact;
                System.out.println("Name: " + person.getName());
                System.out.println("Surname: " + person.getSurname());
                System.out.println("Birth date: " + person.getBirth_date());
                System.out.println("Gender: " + person.getGender());
                System.out.println("Number: " + person.getPhoneNumber());
                System.out.println("Time created: " + person.getTimeCreated());
                System.out.println("Time last edit: " + person.getTimeUpdated());
            } else {
                Organization organization = (Organization) contact;
                System.out.println("Organization name: " + organization.getName());
                System.out.println("Address: " + organization.getAddress());
                System.out.println("Number: " + organization.getPhoneNumber());
                System.out.println("Time created: " + organization.getTimeCreated());
                System.out.println("Time last edit: " + organization.getTimeUpdated());

            }
        }

        void exit() {
            this.filling = false;
        }

    }

    abstract static class Contact {
        Field[] returnFields() {
            try {
                return Class.forName(this.getClassName()).getDeclaredFields();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return new Field[0];
        }

        ;

        void changeField(String field, String value) {
            try {
                Class.forName(getClassName()).getDeclaredField(field).set(this, value);
            } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        ;

        String getField(String field) {
            try {
                return String.valueOf(Class.forName(getClassName()).getDeclaredField(field).get(this));
            } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        ;

        String getClassName() {
            return this.getClass().getName();
        }

        private static String phoneNumber;
        private static LocalDateTime timeCreated;
        private static LocalDateTime timeUpdated;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        Contact() {
            Contact.timeCreated = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            Contact.timeUpdated = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        }
        static void setPhoneNumber(String phoneNumber) {
            Pattern pattern = Pattern.compile
                    ("([+]?[\\da-zA-Z]+([\\s-])?([\\da-zA-Z]{2,})?([\\s-])?([\\da-zA-Z]{2,})?([\\s-])?([\\da-zA-Z]{2,})?([\\s-])?([\\da-zA-Z]{2,})?)|" +
                            "([+]?[(][\\da-zA-Z]+[)])|" +
                            "([+]?[(][\\da-zA-Z]+?[)][\\s-]([\\da-zA-Z]{2,})?([\\s-])?([\\da-zA-Z]{2,})?([\\s-])?([\\da-zA-Z]{2,})?([\\s-])?([\\da-zA-Z]{2,})?)|" +
                            "([+]?([\\da-zA-Z]+)?([\\s-])[(]([\\da-zA-Z]{2,})[)]([\\s-])?([\\da-zA-Z]{2,})?([\\s-])?([\\da-zA-Z]{2,})?([\\s-])?([\\da-zA-Z]{2,})?)");
            Matcher matcher = pattern.matcher(phoneNumber);
            if (matcher.matches()) {
                Contact.phoneNumber = phoneNumber;
            } else {
                System.out.println("Wrong number format!");
                Contact.phoneNumber = "[no number]";
            }
        }

        public static String getPhoneNumber() {
            return phoneNumber;
        }

        public static LocalDateTime getTimeCreated() {
            return timeCreated;
        }

        public static LocalDateTime getTimeUpdated() {
            return timeUpdated;
        }

        public static void setTimeUpdated(LocalDateTime timeUpdated) {
            Contact.timeUpdated = timeUpdated;
        }
    }

    static class Organization extends Contact {
        public String getAddress() {
            return address;
        }

        private String name;
        private String address;
        private String phoneNumber;
        private LocalDateTime timeCreated;
        private LocalDateTime timeUpdated;

//        @Override
//        Field[] returnFields() {
//            try {
//                return Class.forName("contacts.Main$Organization").getSuperclass().getDeclaredFields();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            return new Field[0];
//        }
//
//        @Override
//        void changeField(String field, String value) {
//            try {
//                Class.forName("contacts.Main$Organization").getDeclaredField(field).set(this, value);
//            } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        String getField(String field) {
//            try {
//                return String.valueOf(Class.forName("contacts.Main$Organization").getDeclaredField(field).get(this));
//            } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }

        Organization() {
            timeCreated=getTimeCreated();
            timeUpdated=getTimeUpdated();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the organization name:");
            setName(scanner.nextLine());
            name=getName();
            System.out.println("Enter the address:");
            setAddress(scanner.nextLine());
            System.out.println("Enter the number:");
            setPhoneNumber(scanner.nextLine());
            phoneNumber=getPhoneNumber();
            System.out.println("The record added.");
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    static class Person extends Contact {
        private String name;
        private String surname;
        private String birth_date;
        private String gender;
        private String phoneNumber;
        private LocalDateTime timeCreated;
        private LocalDateTime timeUpdated;

//        @Override
//        Field[] returnFields() {
//            try {
//                return Class.forName("contacts.Main$Person").getSuperclass().getDeclaredFields();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            return new Field[0];
//        }
//
//        @Override
//        void changeField(String field, String value) {
//            try {
//                Class.forName("contacts.Main$Person").getDeclaredField(field).set(this, value);
//            } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        String getField(String field) {
//            try {
//                return String.valueOf(Class.forName("contacts.Main$Person").getDeclaredField(field).get(this));
//            } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }

        Person() {
            timeCreated=getTimeCreated();
            timeUpdated=getTimeUpdated();
            System.out.println("Enter the name:");
            Scanner scanner = new Scanner(System.in);
            setName(scanner.nextLine());
            System.out.println("Enter the surname:");
            name=getName();
            setSurname(scanner.nextLine());
            surname=getSurname();
            System.out.println("Enter the birth date:");
            setBirth_date(scanner.nextLine());
            birth_date=getBirth_date();
            System.out.println("Enter the gender (M, F):");
            setGender(scanner.nextLine());
            gender=getGender();
            System.out.println("Enter the number:");
            setPhoneNumber(scanner.nextLine());
            phoneNumber=getPhoneNumber();
            System.out.println("The record added.");
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            switch (gender) {
                case "M":
                    this.gender = gender;
                    break;
                case "F":
                    this.gender = gender;
                    break;
                default:
                    System.out.println("Bad gender!");
                    this.gender = "[no data]";
                    break;
            }
        }

        public String getBirth_date() {
            return birth_date;
        }

        public void setBirth_date(String birth_date) {
            try {
                LocalDate localDate = LocalDate.parse(birth_date);
                this.birth_date = String.valueOf(localDate);
            } catch (Exception e) {
                System.out.println("Bad birth date!");
                this.birth_date = "[no data]";
            }
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

    }

    public static void main(String[] args) {
        Contacts contacts = new Contacts();
    }
}
