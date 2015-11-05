
public class TestEncriptar {

	public static void main(String[] args) {
		Mickey mickey = new Mickey();
		
		int[] key = new int[80];
		int[] iv = new int[80];
		
		int[] input = new int[160];
		int[] output = new int[512];//Es un tamaño grande para una prueba, solo se va a guardar valores
		
		//Inicializaciones
		for(int i=0;i<80;i++)
		{
			key[i] = 0;
			iv[i] = 0;
		}
		
		byte[] aux = "clave segura".getBytes();
		
		for(int i=0;i<aux.length;i++)
		{
			key[i] = aux[i];		
		}
		
		for(int i=0;i<160;i++)
		{
			input[i] = 0;
		}
		
		byte[] aux2 = "Texto claro".getBytes();
		
		for(int i=0;i<aux2.length;i++)
		{
			input[i] = aux2[i];		
		}
		
		/*Pasos a seguir
		 * 
		 * 
		 * 1- Initialise the registers R and S with all zeros.
		 * 2- (Load in IV .)
		 * 3- (Load in K .) 
		 * 4- (Preclock.)
		 * 5- Encrypt
		 * 
		 */
		
		//Paso 1
		Ctx ctx = new Ctx();//Inicializa to oen 0
		
		//Paso 2 y 3
		mickey.ECRYPT_keysetup(ctx, key, key.length, iv.length);
		mickey.ECRYPT_ivsetup(ctx, iv);		
		
		//Encriptar /* 0 = encrypt; 1 = decrypt; */
		mickey.ECRYPT_process_bytes(0, ctx, input, output, input.length);
		
		System.out.print(output.toString());		


	}

}
