
public class Test {

	public static void main(String[] args) {
		Mickey mickey = new Mickey();
		
		byte[] key = new byte[80];
		byte[] iv = new byte[80];
		
		byte[] input = new byte[160];
		byte[] output = new byte[512];//Es un tamaño grande para una prueba, solo se va a guardar valores
		
		//Inicializaciones
		key = "clave segura".getBytes();
		input = "Texto claro".getBytes();
		
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
		
		

	}

}
