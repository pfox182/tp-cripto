
public class TestEjemplo {

	public static void main(String[] args) {
		int[] key_1 = {0x12, 0x34, 0x56, 0x78, 0x9a, 0xbc, 0xde, 0xf0, 0x12, 0x34};
		int[] key_2 = {0xf1, 0x1a, 0x56, 0x27, 0xce, 0x43, 0xb6, 0x1f, 0x89, 0x12};
		int[] key_3 = {0x3b, 0x80, 0xfc, 0x8c, 0x47, 0x5f, 0xc2, 0x70, 0xfa, 0x26};
		int[] key_4 = {0x82, 0xac, 0xb3, 0x8c, 0x5d, 0x7a, 0x3c, 0x78, 0xd9, 0x8f};
		int[] iv_1  = {0x21, 0x43, 0x65, 0x87};
		int[] iv_2  = {0x9c, 0x53, 0x2f, 0x8a, 0xc3, 0xea, 0x4b, 0x2e, 0xa0, 0xf5};

	    /* Initialise the algorithm */
		//ECRYPT_init();//Solo retorna true

	    /* Generate the test data */
	    //perform_test(key_1, iv_1, 32); //Original
		perform_test(key_1, iv_1, iv_1.length);
	    perform_test(key_2, iv_2, iv_2.length);
	    perform_test(key_3, null, 0);
	    //perform_iterated_test(key_4);

	}
	
	/* The following routine generates 16 bytes of keystream from key and iv, and
	   displays key, iv and keystream */

	public static void perform_test (int[] key, int[] iv, int iv_length_in_bits)
	{
		Mickey mickey = new Mickey();
	    Ctx ctx = new Ctx();
	    
	        /* Keystream generator context */
	    int[] keystream = new int[16];
	        /* Array to contain generated keystream bytes */
	    int i;
	        /* Counting variable */

	    /* Load key */
	    //mickey.ECRYPT_keysetup (ctx, key, 80, iv_length_in_bits); //Original
	    mickey.ECRYPT_keysetup (ctx, key, key.length, iv_length_in_bits);
	    /* Load IV */
	    mickey.ECRYPT_ivsetup (ctx, iv);
	    /* Generate keystream */
	    //mickey.ECRYPT_keystream_bytes(ctx, keystream, 16); //Original
	    int[] output_keystream = mickey.ECRYPT_keystream_bytes(ctx, keystream, keystream.length);
	    
	    /* Display the key */
	    System.out.printf("Key               =");
	    for (i=0; i<key.length; i++)
	    {
	    	System.out.printf(" %02x", key[i]);
	    }
	    System.out.printf("\n");

	    /* Display the IV */
	    System.out.printf("IV                =");
	    for (i=0; i<iv_length_in_bits; i++)
	    {
	    	System.out.printf(" %02x", iv[i]);
	    }
	    System.out.printf("\n");

	    /* Display the derived keytream */
	    System.out.printf("Keystream         =");
	    for (i=0; i<output_keystream.length; i++)
	    {
	    	System.out.printf(" %02x", output_keystream[i]);
	    }
	    System.out.printf("\n");

	    System.out.printf("\n");

	}

	/* The following routine repeatedly reloads the keystream generator with key and iv
	   formed from keystream from a previous load of the generator.  */

	public static void perform_iterated_test (int[] key)
	{
		Mickey mickey = new Mickey();
		
	    Ctx ctx = new Ctx();
	    
	        /* Keystream generator context */
	    int[] iv = new int[4];
	        /* Array to contain iv derived from keystream */
	    int[] keystream = new int[16];
	        /* Array to contain generated keystream bytes */
	    int i;
	        /* Counting variable */

	    /* Display the key */
	    System.out.printf("Iterated test key =");
	    for (i=0; i<10; i++)
	    {
	    	System.out.printf(" %02x", key[i]);
	    }
	    System.out.printf("\n");

	    /* Load key */
	    mickey.ECRYPT_keysetup(ctx, key, 80, 0);
	    mickey.ECRYPT_ivsetup(ctx, iv);

	    for (i=0; i<1000; i++)
	    {
	        /* Generate new key and iv from keystream */
	    	mickey.ECRYPT_keystream_bytes(ctx, key, 10);
	    	mickey.ECRYPT_keystream_bytes(ctx, iv, 4);

	        /* Load new key */
	    	mickey.ECRYPT_keysetup(ctx, key, 80, 32);

	        /* Load new IV */
	    	mickey.ECRYPT_ivsetup(ctx, iv);
	    }

	    /* Generate keystream */
	    mickey.ECRYPT_keystream_bytes(ctx, keystream, 16);

	    /* Display the derived keytream */
	    System.out.printf("Final keystream   =");
	    for (i=0; i<16; i++)
	    {
	    	System.out.printf(" %02x", keystream[i]);
	    }
	    System.out.printf("\n");

	    System.out.printf("\n");

	}

}
