//
//  main.c
//  TP-criptografia
//
//  Created by Martin Cattaneo on 11/11/15.
//  Copyright © 2015 Martin Cattaneo. All rights reserved.
//

#include <stdio.h>

typedef struct
{
    unsigned char R[100];
    /* The 80 bit linear register R  */
    unsigned char S[100];
    /* The 80 bit non-linear register S  */
    unsigned char key[10];
    /* The 10 byte base key.  This is stored in the context because it must be
     reloaded into the keystream generator each time an IV is loaded */
    unsigned int ivsize;
    /* The IV length, which may be between 0 and 80 (but must be fixed while
		   the generator is used with any one secret key */
    /*
     * [edited]
     *
     * Put here all state variable needed during the encryption process.
     */
} ECRYPT_ctx;

unsigned char R_Mask[100] = {1,1,0,1,1,1,1,0,0,1,0,0,1,1,0,0,1,0,0,1,1,1,1,0,0,
    1,0,0,1,0,0,0,0,0,0,0,0,1,1,0,0,1,1,0,0,1,1,0,0,0,
    1,0,1,0,1,0,1,0,1,0,1,1,0,1,1,1,1,1,0,0,0,1,1,0,0,
    0,0,0,0,1,1,1,1,0,0,0,0,1,1,1,1,1,1,0,1,1,1,1,0,0};
/* Feedback mask associated with the register R (independent of key or IV) */

unsigned char COMP0[99]   = {0,0,0,0,1,1,0,0,0,1,0,1,1,1,1,0,1,0,0,1,0,1,0,1,0,
    1,0,1,0,1,1,0,1,0,0,1,0,0,0,0,0,0,0,1,0,1,0,1,0,1,
    0,0,0,0,1,0,1,0,0,1,1,1,1,0,0,1,0,1,0,1,1,1,1,1,1,
    1,1,1,0,1,0,1,1,1,1,1,1,0,1,0,1,0,0,0,0,0,0,1,1};
unsigned char COMP1[99]   = {0,1,0,1,1,0,0,1,0,1,1,1,1,0,0,1,0,1,0,0,0,1,1,0,1,
    0,1,1,1,0,1,1,1,1,0,0,0,1,1,0,1,0,1,1,1,0,0,0,0,1,
    0,0,0,1,0,1,1,1,0,0,0,1,1,1,1,1,1,0,1,0,1,1,1,0,1,
    1,1,1,0,0,0,1,0,0,0,0,1,1,1,0,0,0,1,0,0,1,1,0,0};
/* Determines whether certain bits are complemented when used in the S feedback function */

unsigned char FB0[100]    = {1,1,1,1,0,1,0,1,1,1,1,1,1,1,1,0,0,1,0,1,1,1,1,1,1,
    1,1,1,1,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,1,0,0,1,0,1,
    0,1,0,0,1,0,1,1,1,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,
    1,1,0,1,0,0,0,1,1,0,1,1,1,0,0,1,1,1,0,0,1,1,0,0,0};
unsigned char FB1[100]    = {1,1,1,0,1,1,1,0,0,0,0,1,1,1,0,1,0,0,1,1,0,0,0,1,0,
    0,1,1,0,0,1,0,1,1,0,0,0,1,1,0,0,0,0,0,1,1,0,1,1,0,
    0,0,1,0,0,0,1,0,0,1,0,0,1,0,1,1,0,1,0,1,0,0,1,0,1,
    0,0,0,1,1,1,1,0,1,1,1,1,1,0,0,0,0,0,0,1,0,0,0,0,1};
/* Two alternative sets of stages into which s99 is fed back, Galois-style */


void CLOCK_R(ECRYPT_ctx* ctx,unsigned char input_bit_r,unsigned char control_bit_r)
{
    unsigned char Feedback_bit;
    /* r_99 ^ input bit */
    unsigned char i;
    /* Index variable */
    
    Feedback_bit = ctx->R[99] ^ input_bit_r;
    
    if (control_bit_r)
    {
        /* Shift and XOR */
        if (Feedback_bit)
        {
            for (i=99; i>0; i--)
                ctx->R[i] = ctx->R[i-1] ^ ctx->R[i] ^ R_Mask[i];
            ctx->R[0] = R_Mask[0] ^ ctx->R[0];
        }
        else
        {
            for (i=99; i>0; i--)
                ctx->R[i] = ctx->R[i-1] ^ ctx->R[i];
        }
    }
    else
    {
        /* Shift only */
        if (Feedback_bit)
        {
            for (i=99; i>0; i--)
                ctx->R[i] = ctx->R[i-1] ^ R_Mask[i];
            ctx->R[0] = R_Mask[0];
        }
        else
        {
            for (i=99; i>0; i--)
                ctx->R[i] = ctx->R[i-1];
            ctx->R[0] = 0;
        }
    }
    
}

void CLOCK_S(ECRYPT_ctx* ctx,unsigned char input_bit_s,unsigned char control_bit_s)
{
    unsigned char s_hat[100];
    /* Intermediate values of the s stages */
    unsigned char Feedback_bit;
    /* s_99 ^ input bit */
    int i;
    /* Index variable */
    
    Feedback_bit = ctx->S[99] ^ input_bit_s;
    
    for (i=98; i>0; i--)
        s_hat[i] = ctx->S[i-1] ^ ((ctx->S[i] ^ COMP0[i]) & (ctx->S[i+1] ^ COMP1[i]));
    s_hat[0] = 0;
    s_hat[99] = ctx->S[98];
    
    for (i=0; i<100; i++)
        ctx->S[i] = s_hat[i];
    if (Feedback_bit)
    {
        if (control_bit_s)
        {
            for (i=0; i<100; i++)
                ctx->S[i] = s_hat[i] ^ FB1[i];
        }
        else
        {
            for (i=0; i<100; i++)
                ctx->S[i] = s_hat[i] ^ FB0[i];
        }
    }
}

int CLOCK_KG(ECRYPT_ctx* ctx,unsigned char mixing,unsigned char input_bit)
{
    int Keystream_bit;
    /* Keystream bit to be returned */
    unsigned char Control_bit_R, Control_bit_S;
    /* Control the variable clocking of the R and S registers */
    
    Keystream_bit = (ctx->R[0] ^ ctx->S[0]) & 1;
    Control_bit_R = ctx->S[34] ^ ctx->R[67];
    Control_bit_S = ctx->S[67] ^ ctx->R[33];
    if (mixing)
        CLOCK_R (ctx, input_bit ^ ctx->S[50], Control_bit_R);
    else
        CLOCK_R (ctx, input_bit, Control_bit_R);
    CLOCK_S (ctx, input_bit, Control_bit_S);
    
    return Keystream_bit;
}

void ECRYPT_keysetup(ECRYPT_ctx* ctx,const unsigned char* key,unsigned int keysize, unsigned int ivsize)
{
    int i;
    /* Indexing variable */
    
    /* Store the key in the algorithm context */
    for (i = 0; i<10; i++)
        ctx->key[i] = key[i];
    
    /* Remember the IV size */
    ctx->ivsize = ivsize;
}

void ECRYPT_ivsetup(ECRYPT_ctx* ctx,const unsigned char* iv)
{
    unsigned char i;
    /* Counting/indexing variable */
    unsigned char iv_or_key_bit;
    /* Bit being loaded */
    
    
    /* Initialise R and S to all zeros */
    for (i=0; i<100; i++)
    {
        ctx->R[i] = 0;
        ctx->S[i] = 0;
    }
    
    /* Load in IV */
    for (i=0; i<ctx->ivsize; i++)
    {
        iv_or_key_bit = (iv[i/8] >> (7-(i%8))) & 1; /* Adopt usual, perverse, labelling order */
        CLOCK_KG (ctx, 1, iv_or_key_bit);
    }
    
    /* Load in K */
    for (i=0; i<80; i++)
    {
        iv_or_key_bit = (ctx->key[i/8] >> (7-(i%8))) & 1; /* Adopt usual, perverse, labelling order */
        CLOCK_KG (ctx, 1, iv_or_key_bit);
    }
    
    /* Preclock */
    for (i=0; i<100; i++)
    {
        CLOCK_KG (ctx, 1, 0);
    }
}

void ECRYPT_process_bytes(int action,ECRYPT_ctx* ctx,const unsigned char* input,unsigned char* output,unsigned int msglen){
    unsigned int i, j;
    /* Counting variables */
    
    for (i=0; i<msglen; i++)
    {
        output[i] = input[i];
        
        for (j=0; j<8; j++)
            output [i] ^= CLOCK_KG (ctx, 0, 0) << (7-j);
    }
}

void ECRYPT_keystream_bytes(ECRYPT_ctx* ctx, unsigned char* keystream,unsigned int length)
{
    unsigned int i, j;
    /* Counting variables */
    
    for (i=0; i<length; i++)
    {
        keystream[i] = 0;
        
        for (j=0; j<8; j++)
            keystream[i] ^= CLOCK_KG (ctx, 0, 0) << (7-j);
    }
}

unsigned char* perform_test (unsigned char *key, unsigned char* iv, int iv_length_in_bits)
{
    ECRYPT_ctx ctx;
    /* Keystream generator context */
    unsigned char keystream[16];
    /* Array to contain generated keystream bytes */
    int i;
    /* Counting variable */
    
    /* Load key */
    ECRYPT_keysetup (&ctx, key, 80, iv_length_in_bits);
    /* Load IV */
    ECRYPT_ivsetup (&ctx, iv);
    /* Generate keystream */
    ECRYPT_keystream_bytes (&ctx, keystream, 16);
    
    /* Display the key */
    printf ("Key               =");
    for (i=0; i<10; i++)
        printf (" %02x", key[i]);
    printf ("\n");
    
    /* Display the IV */
    printf ("IV                =");
    for (i=0; i<(iv_length_in_bits+7)/8; i++)
        printf (" %02x", iv[i]);
    printf ("\n");
    
    /* Display the derived keytream */
    printf ("Keystream         =");
    for (i=0; i<16; i++)
        printf (" %02x", keystream[i]);
    printf ("\n");
    
    return keystream;
    
}

void perform_custom_test_encrypt (unsigned char *keystream,const unsigned char* input, unsigned char* output)
{
    int i;
    
    for (i=0; i<16; i++)
        output[i] = input[i] ^ keystream[i];
    
    printf ("Input text:=");
    for (i=0; i<16; i++)
        printf (" %c", input[i]);
    printf ("\n");
    
    printf ("Output text:=");
    for (i=0; i<16; i++)
        printf (" %c", output[i]);
    printf ("\n");
    printf ("\n");
    printf ("\n");
    
}





int main(int argc, const char * argv[]) {
    
    unsigned char key_1[10] = {0x12, 0x34, 0x56, 0x78, 0x9a, 0xbc, 0xde, 0xf0, 0x12, 0x34};
    unsigned char key_2[10] = {0xf1, 0x1a, 0x56, 0x27, 0xce, 0x43, 0xb6, 0x1f, 0x89, 0x12};
    unsigned char key_3[10] = {0x3b, 0x80, 0xfc, 0x8c, 0x47, 0x5f, 0xc2, 0x70, 0xfa, 0x26};
    unsigned char key_4[10] = {0x82, 0xac, 0xb3, 0x8c, 0x5d, 0x7a, 0x3c, 0x78, 0xd9, 0x8f};
    unsigned char iv_1[4]   = {0x21, 0x43, 0x65, 0x87};
    unsigned char iv_2[10]  = {0x9c, 0x53, 0x2f, 0x8a, 0xc3, 0xea, 0x4b, 0x2e, 0xa0, 0xf5};
    
    
    /* Generate the test data */
    int i;
    unsigned char* keystream = perform_test(key_1, iv_1, 32);
    unsigned char input[16] = {'h','o','l','a','\0','0','0','0','0','0','0','0','0','0','0','0'};
    unsigned char output[16];
    unsigned char output2[16];
    
    perform_custom_test_encrypt(keystream,input,output);
    perform_custom_test_encrypt(keystream,output,output2);
    //perform_test (key_2, iv_2, 80);
    //perform_test (key_3, NULL, 0);
    
    return 0;
}
