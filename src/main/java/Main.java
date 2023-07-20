import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import model.Booking;
import model.BookingDates;
import model.BookingResponse;
import model.builder.BookingBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import static org.assertj.core.api.Assertions.*;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        
        Booking bookingRequest = new BookingBuilder()
                .withFirstname("Adam")
                .withLastname("Sajewicz")
                .withTotalprice(1500)
                .withDepositpaid(false)
                .withAdditionalneeds("WiFi")
                .withBookingdates(new BookingDates("2023-08-19", "2023-09-01"))
                .build();

        Gson gson = new Gson();
        String bookingJsonRequest = gson.toJson(bookingRequest);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("https://restful-booker.herokuapp.com/booking"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(bookingJsonRequest))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(httpResponse.statusCode())
                .withFailMessage("StatusCode is incorrect!")
                .isEqualTo(200);
        log.info("Successfully verified that the StatusCode is equal to: " + 200);
        log.info(httpResponse.body());

        BookingResponse bookingResponse = gson.fromJson(httpResponse.body(), BookingResponse.class);

        log.info("Just to double-check, the recorded booking's additional needs are: " + bookingResponse.getBooking().getAdditionalneeds());
    }
}
