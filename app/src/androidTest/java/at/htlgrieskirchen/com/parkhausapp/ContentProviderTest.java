package at.htlgrieskirchen.com.parkhausapp;

import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

/**
 * Created by thofer
 */
public class ContentProviderTest extends ProviderTestCase2<MyContentProvider> {

    private static MockContentResolver resolve;

    /**
     * Constructor.
     *
     * @param providerClass     The class name of the provider under test
     * @param providerAuthority The provider's authority string
     */
    public ContentProviderTest(Class<MyContentProvider> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public ContentProviderTest()
    {
        super(MyContentProvider.class, "at.htlgrieskirchen.com.parkhausapp.MyContentProvider");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolve = this.getMockContentResolver();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testen()
    {
        String[] projection = {"Name", "Standort", "Preis"};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        //mit richtiger URI
        Cursor result = resolve.query(Uri.parse("content://at.htlgrieskirchen.com.parkhausapp.MyContentProvider/parkhaeuser"), projection, selection, selectionArgs, sortOrder);
        assertEquals(result.getColumnCount(), 3);
    }
}