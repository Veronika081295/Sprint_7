package praktikum;

import net.datafaker.Faker;

public class CourierRandomDataGenerator {
    static Faker faker = new Faker();

    public CourierInfo generateRandomDataForCourierCreation() {
        return new CourierInfo(
                faker.name().name(),
                faker.internet().password(),
                faker.name().firstName()
        );
    }
}
