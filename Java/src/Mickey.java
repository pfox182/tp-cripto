
public class Mickey {
	/* Feedback mask associated with the register R (independent of key or IV) */
	public int[] R_Mask = {1,0,0,0,1,1,0,0,1,0,1,1,0,0,1,0,1,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,
            1,0,0,1,1,0,1,0,0,0,1,1,0,0,1,0,0,0,1,1,0,1,1,1,1,1,0,0,1,1,1,1,
            0,1,1,0,0,1,0,0,0,1,1,0,1,0,0,1,1,1,1,0,0,1,1,0,0,0,1,1,1,0,0,1,
            0,1,0,0,1,1,0,0,0,1,1,1,1,1,0,1,1,1,0,1,1,1,0,0,0,0,0,0,0,0,0,1,
            1,1,1,1,0,1,0,1,1,1,0,0,1,0,1,0,0,1,0,0,1,0,1,0,1,1,1,0,1,1,0,0};
	
	/* Determines whether certain bits are complemented when used in the S feedback function */
	public int[] COMP0 = {0,1,1,1,1,0,1,0,0,1,0,0,1,1,1,1,0,1,1,0,1,0,1,1,1,0,1,1,1,0,1,0,
            1,0,1,0,1,0,1,0,1,0,0,1,0,0,0,0,0,1,1,0,0,1,0,0,1,0,0,1,1,1,1,0,
            0,1,0,0,0,1,1,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,1,0,0,1,1,1,1,0,
            1,0,0,0,1,1,0,0,1,0,0,1,1,0,1,1,1,1,1,1,0,1,0,1,1,1,1,0,1,1,0,0,
            0,1,1,1,1,1,0,1,0,1,1,0,0,0,0,0,0,1,1,1,1,1,0,1,1,1,1,1,0,0,0};
	public int[] COMP1 = {0,0,0,0,1,1,0,0,1,1,1,1,1,0,0,0,1,0,0,1,1,0,0,0,1,0,1,1,1,1,1,0,
	            0,0,0,1,1,0,0,1,0,0,1,1,1,1,0,0,0,1,1,0,1,1,0,1,0,1,1,1,1,1,1,1,
	            0,0,0,0,0,1,1,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,
	            1,0,1,0,0,0,1,0,1,1,0,0,0,1,1,1,0,0,0,0,0,1,1,0,0,1,1,0,0,1,1,0,
	            1,0,1,0,1,1,0,1,1,1,0,1,1,0,1,0,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1};
	
	/* Two alternative sets of stages into which s159 is fed back, Galois-style */
	public int[] FB0  = {1,1,1,1,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,1,1,1,0,0,0,0,1,0,0,0,1,1,
	            0,1,0,0,0,1,0,0,1,1,0,0,0,1,0,1,1,1,1,1,0,1,0,0,0,1,1,1,0,0,0,0,
	            1,0,0,0,0,0,0,1,1,0,1,1,0,0,1,0,1,0,1,0,0,1,1,1,0,1,1,0,0,1,1,0,
	            1,0,0,0,1,0,0,1,1,1,0,1,0,0,1,0,0,0,1,0,1,0,1,0,0,0,1,0,1,0,1,1,
	            1,0,0,0,0,0,1,1,1,1,0,1,0,0,0,0,1,1,0,0,0,1,1,0,1,1,0,0,0,0,0,1};
	public int[] FB1  = {1,1,0,1,0,1,0,1,1,1,1,0,1,1,1,0,0,0,1,0,1,1,1,1,1,1,0,1,1,0,0,1,
	            0,0,0,0,1,0,0,1,0,0,1,1,0,0,0,1,1,0,0,1,1,1,1,0,0,0,0,0,1,1,1,0,
	            0,1,1,0,1,1,0,1,0,0,0,1,1,0,0,0,0,1,0,1,1,0,0,1,1,1,1,1,0,1,1,0,
	            1,1,1,0,0,1,1,1,0,1,1,1,1,1,1,0,1,1,0,1,0,0,1,0,0,0,1,1,0,1,1,0,
	            1,1,1,1,0,1,1,1,0,0,0,0,0,0,0,1,1,1,1,0,0,1,0,1,1,0,0,0,1,0,0,0};
	
	public void CLOCK_R(Ctx ctx, int input_bit_r,int control_bit_r)	{
		    int Feedback_bit;
		        /* r_159 ^ input bit */
		    int i;
		        /* Index variable */

		    Feedback_bit = ctx.R[159] ^ input_bit_r;

		    if (control_bit_r > 1)
		    {
		        /* Shift and XOR */
		        if (Feedback_bit > 1)
		        {
		            for (i=159; i>0; i--)
		                ctx.R[i] = ctx.R[i-1] ^ ctx.R[i] ^ R_Mask[i];
		            ctx.R[0] = R_Mask[0] ^ ctx.R[0];
		        }
		        else
		        {
		            for (i=159; i>0; i--)
		                ctx.R[i] = ctx.R[i-1] ^ ctx.R[i];
		        }
		    }
		    else
		    {   
		        /* Shift only */
		        if (Feedback_bit > 1)
		        {
		            for (i=159; i>0; i--)
		                ctx.R[i] = ctx.R[i-1] ^ R_Mask[i];
		            ctx.R[0] = R_Mask[0];
		        }
		        else
		        {
		            for (i=159; i>0; i--)
		                ctx.R[i] = ctx.R[i-1];
		            ctx.R[0] = 0;
		        }
		    }
		    
		}
	
	/* The following routine clocks register S in ctx with given input and control bits */

	public void CLOCK_S(Ctx ctx, int input_bit_s,int control_bit_s){
	    int[] s_hat = new int[160];
			/* Intermediate values of the s stages */
		int Feedback_bit;
	        /* s_159 ^ input bit */
	    int i;
	        /* Index variable */

	    Feedback_bit = ctx.S[159] ^ input_bit_s;

	    for (i=158; i>0; i--)
	    {
	        s_hat[i] = ctx.S[i-1] ^ ((ctx.S[i] ^ COMP0[i]) & (ctx.S[i+1] ^ COMP1[i]));
	    }
		s_hat[0] = 0;
		s_hat[159] = ctx.S[158];

	    for (i=0; i<160; i++)
	    {
	    	ctx.S[i] = s_hat[i];
	    }
	    
		if (Feedback_bit > 1)
	    {
	        if (control_bit_s > 1)
	        {
	            for (i=0; i<160; i++)
	                ctx.S[i] = s_hat[i] ^ FB1[i];
	        }else{
	            for (i=0; i<160; i++)
	                ctx.S[i] = s_hat[i] ^ FB0[i];
	        }
	    }
	}

	/* The following routine obtains a keysteam bit from the generator, which it then clocks */

	public int CLOCK_KG(Ctx ctx,int mixing,int input_bit){
	    int Keystream_bit;
	        /* Keystream bit to be returned */
		int Control_bit_R, Control_bit_S;
			/* Control the variable clocking of the R and S registers */

	    Keystream_bit = (ctx.R[0] ^ ctx.S[0]) & 1;
		Control_bit_R = ctx.S[54] ^ ctx.R[106];
		Control_bit_S = ctx.S[106] ^ ctx.R[53];
		
	    if (mixing > 1){
			CLOCK_R(ctx, input_bit ^ ctx.S[80], Control_bit_R);
		}else{
			CLOCK_R(ctx, input_bit, Control_bit_R);
	    	CLOCK_S(ctx, input_bit, Control_bit_S);
		}

	    return Keystream_bit;
	}
	
	/* Key setup: simply save the key in ctx for use during IV setup */

	public void ECRYPT_keysetup(Ctx ctx,byte[] key,/* Key size in bits. */ long keysiz,/* IV size in bits. */ long ivsize)                 
	{
	    int i; /* Indexing variable */	       

	    /* Store the key in the algorithm context */
	    for (i = 0; i<16; i++)
	    {
	    	ctx.key[i] = key[i];
	    }

		/* Remember the IV size */
		ctx.ivsize = ivsize;
	}
	
	/*
	 * IV setup. After having called ECRYPT_keysetup(), the user is
	 * allowed to call ECRYPT_ivsetup() different times in order to
	 * encrypt/decrypt different messages with the same key but different
	 * IV's.
	 */

	/* This routine implements key loading according to the MICKEY-128 specification */

	public void ECRYPT_ivsetup(Ctx ctx,byte[] iv)
	{
	    int i;
	        /* Counting/indexing variable */
	    int iv_or_key_bit;
	        /* Bit being loaded */


	    /* Initialise R and S to all zeros */
	    for (i=0; i<160; i++)
	    {
	        ctx.R[i] = 0;
	        ctx.S[i] = 0;
	    }

	    /* Load in IV */
	    for (i=0; i<ctx.ivsize; i++)
	    {
	        iv_or_key_bit = (iv[i/8] >> (7-(i%8))) & 1; /* Adopt usual, perverse, labelling order */
	        CLOCK_KG (ctx, 1, iv_or_key_bit);
	    }

	    /* Load in K */
	    for (i=0; i<128; i++)
	    {
	        iv_or_key_bit = (ctx.key[i/8] >> (7-(i%8))) & 1; /* Adopt usual, perverse, labelling order */
	        CLOCK_KG (ctx, 1, iv_or_key_bit);
	    }

	    /* Preclock */
	    for (i=0; i<160; i++)
	    {
	        CLOCK_KG (ctx, 1, 0);
	    }
	}
	
	/* Stream cipher a block of data */

	public void ECRYPT_process_bytes(int action,/* 0 = encrypt; 1 = decrypt; */Ctx ctx,byte[] input, byte[] output,int msglen/* length in bytes */){
		int i, j;
	        /* Counting variables */

	    for (i=0; i<msglen; i++)
	    {
	        output[i] = input[i];

	        for (j=0; j<8; j++)
	        {
	        	output [i] ^= CLOCK_KG (ctx, 0, 0) << (7-j);
	        }
	    }
	}
	
	/* Generate keystream data */

	public void ECRYPT_keystream_bytes(Ctx ctx,int[] keystream,int length) /* Length of keystream in bytes. */
	{
	    int i, j;
	        /* Counting variables */

	    for (i=0; i<length; i++)
	    {
	        keystream[i] = 0;

	        for (j=0; j<8; j++)
	        {
	        	keystream[i] ^= CLOCK_KG (ctx, 0, 0) << (7-j);
	        }
	    }
	}
}
