package cli;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rest.RESTClient;

@ExtendWith(MockitoExtension.class)
public class AirportCLIApplicationTest {
    @Mock
    private RESTClient mockRESTClient;
}