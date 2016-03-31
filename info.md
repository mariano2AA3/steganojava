# TMI

## Diagramas de flujo

### ENCODE

 * Se selecciona una imagen:
    * Se comprueba si el formato está adminitido por la app => Error
    * Se calcula el espacio disponible (segun el algoritmo, por defecto es LSB) para almacenar la info.
 
 * Se selecciona un fichero:
	* Se comprueba si entra en la imagen seleccionada => Error
	
 * El usuario selecciona el algoritmo que desea aplicar (recalcular espacio para almacenar si es necesario)
 
 * El usuario escribe si quiere una contraseña:
	* Se comprueba si coinciden => Error
	* Si la escribe, se cifra el fichero con el algoritmo que elijamos (más adelante podemos permitir elegir el alg de cifrado)
		
 * Se aplica el alg de esteganografía seleccionado sobre el fichero original o el fichero cifrado => Error.
 
 * Se escribe en disco con el formato: nombreImagen_secrect.formato, en la misma ruta de la imagen original.
 
 * Se informa a la UI que todo ha ido bien.

 
### DECODE

 * Se selecciona una imagen
	* Se comprueba si el formato está adminitido por la app => Error
 
 * Se permite escribir o no una contraseña
 
 * El usuario especifica el alg para decofidficar (LSB) por defecto.
 
 * Se invoca la funcion para descodificar la imagen con el alg y contraseña (si procede) especificados => Error
 
 * Se escribe el fichero en disco en la misma ruta que la imagen con el formato: secret_reveleaded.FORMATO => Error


## CLases (para los UMls)

### UI (interfaz)

 * Interfaz que define los metodos que todas las interfaces de usuario deben implementar.

 * Metodos:
	* void updateUI(). Actualiza la UI (no necesario para nuestro proyecto, de momento)
	* void showError(msg : String). Muestra un mensaje de error
	* void showMessage(msg : String). Muestra un mensaje de exito
 
 
### MainWindow
 
 * Ventana principal de la UI grafica, hija de UI
 
### Controller

 * Hace de intermediario entre la UI y las diversas funciones de la app.
 
 * Implementara el patron Singleton para instanciar una unica vez los algoritmos esteganograficos.
 
 * Metodos:
	* void encode( img : String, file : String, alg : (Enum | String) , password : String). Aplica el algortimo alg para ocultar el fichero especificado en file en la imagen img.
	* void decode( img : String, alg : (Enum | String) , password : String). Obtiene el fichero oculto de la imagen especificada en img con el algoritmo alg.
	* float analyzeImg ( img : String, alg : (Enum | String) ). Calcula y devuelve el espacio disponible en la imagen img segun el algoritmo alg para almacenar informacion.
	
### Exception
 
 * Clase para manejar las distintas excepciones.
 
 * Hay que decidir si ella sola alberga todas las excepciones o si hacemos que sea la madre y que cada exc. tenga su clase.
 
### Cypher
 
 * Clase que implemeta los metodos para cifrar el fichero que el usuario desea ocultar.
 
 * Metodos:
 
	* void sha256(file : (File | String), password: String)
	* void pgp(file : (File | String), password: String)
	* ...

### File

 * Clase encarga de leer y escribir ficheros en disco.

 * Metodos:
	
	* (void | File | Bytes[]) readFile(file : String). Hay que decidir si devuelve un objeto de tipo File, un array de Bytes...  
	* void writeFile(file: File | Bytes[])
 
### SteganographyAlg

 * Interfaz o Clase abstracta que contiene los metodos que todos los algoritmos esteganograficos deben implementar.
 
 * Tendra una terna de metodos por cada formato de imagen admitido.
 
 * Metodos:
	* void encodeFORMATO()
	* void decodeFORMATO()
	* float analyzeFORMATO()
	* FORMATO puede ser: BMP, JPG, JPEG, PNG ... 
 
