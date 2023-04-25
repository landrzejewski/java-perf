* Implementacja bazy opartej o plik płaski i RandomAccessFile.
  Wymagania:
    * Możliwość wykonywania operacji CRUD na rekordach
    * Możliwość sortowania rekordów po wybranych kolumnach
    * Możliwość wyszukiwania rekordów po wartości kolumny (zawiera, zaczyna się, jest równy, jest większy, jest mniejszy)
      Implementując rozwiązanie zapewnij:
        * Bezpieczeństwo w środowisku wielo-użytkownikowym (synchronizacja wątków)
        * Wydajność dostępu do danych np. tworząc pamięć cache czy tworząc indeks rekordów
          Badaj wydajność swoich pomysłów wykorzystując poznane narzędzia



Przykładowy rekord:
id      firstName    lastName     age,   isActive
long,   String:50,   String:70,   int,   boolean

1 w, 1 m - 100 x CRUD
