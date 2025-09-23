# E-Banking (ΠΛΗ302) — Bank of TUC

Starter repository για την εργασία *Σύστημα Ηλεκτρονικής Τραπεζικής* (ΠΛΗ302, ΣΗΜΜΥ, ΠΚ).

## Περιεχόμενα
- `docs/specs/PLH302.E-BANKING.pdf`: Εκφώνηση/προδιαγραφές εργασίας (αντίγραφο).
- `src/main/java`: Κώδικας εφαρμογής (Java 17, Maven).
  - `app`: entrypoint & configuration
  - `model`: οντότητες (Account, Customer, Company, Transaction, Bill, StandingOrder, κ.λπ.)
  - `dao`: Data Access Objects (CSV/JSON αποθήκευση)
  - `service`: business logic (τοκισμός, μεταφορές, πάγιες εντολές, SEPA/SWIFT mock)
  - `ui/cli`: Command Line διεπαφή
  - `ui/gui`: GUI (Swing/JavaFX — ξεκινά ως placeholder)
- `src/test/java`: βασικά unit tests (JUnit 5)
- `pom.xml`: Maven build (shade plugin για εκτελέσιμο jar)
- `.github/ISSUE_TEMPLATE`: Πρότυπα για issues (bug/feature)

## Τοπική εκτέλεση
```bash
# build
mvn -q -DskipTests package

# run CLI
java -jar target/ebanking-1.0.0.jar --cli

# run GUI (placeholder)
java -jar target/ebanking-1.0.0.jar --gui
```

## Οργάνωση Δεδομένων
- Προτείνεται CSV ή JSON αποθήκευση στο `data/` (θα δημιουργηθεί στην πρώτη εκτέλεση).
- Περιλάβετε sample datasets για όλα τα σενάρια προσομοίωσης (τόκοι, πάγιες, εμβάσματα).

## Άδειες & Συνεισφορές
- MIT License.
- Ανοίξτε issues/PRs με βάση τα templates.

## Οδικός Χάρτης
- [ ] Φάση Ι: Ανάλυση απαιτήσεων & use cases
- [ ] Φάση ΙΙ: Σχεδιασμός (domain model, storyboards)
- [ ] Φάση ΙΙΙ: Υλοποίηση (patterns: Singleton, Factory, Command, Builder, Bridge, DAO)
- [ ] Προσομοίωση ρολογιού (ημερήσιο tick) και εκτέλεση αυτόματων ενεργειών
