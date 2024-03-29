package service.calorie.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 28-10-2019 03:18
 * Purpose: Test
 **/
@RunWith(PowerMockRunner.class)
@PrepareForTest(Utils.class)
public class TestUtils {
    @Test
    public void testCreateErrorJson() {
        assert Utils.createErrorJSON(404, "not found").
                equals("{\"error\":{\"code\":404,\"message\":\"not found\"}}");
    }

    @Test
    public void testCreateJSONErrorResponse() throws IOException {
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);
        PrintWriter mockPrintWriter = Mockito.mock(PrintWriter.class);

        Mockito.when(mockResponse.getWriter()).thenReturn(mockPrintWriter);

        Utils.createJSONErrorResponse(404, "not found", mockResponse);

        Mockito.verify(mockResponse, Mockito.times(1)).
                setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Mockito.verify(mockResponse, Mockito.times(1)).setStatus(404);
        Mockito.verify(mockPrintWriter, Mockito.times(1)).write(Mockito.anyString());

    }

    @Test
    public void testJoinCollection() {
        List<Integer> list = new ArrayList<>();
        // Empty
        assert Utils.joinCollection(list, ",").isEmpty();

        list.add(1);
        // 1 element
        assert Utils.joinCollection(list, ",").equals("1");

        list.add(2);
        list.add(3);
        assert Utils.joinCollection(list, ",").equals("1, 2, 3");

    }
}
