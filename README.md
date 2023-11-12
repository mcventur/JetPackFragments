# Ejemplo de Navegación usando el Jetpack Navigation Component

Este ejemplo está basado en el mismo proyecto de signos del zodiaco que 
[Dynamic Fragment](https://github.com/mcventur/Fragmentos-Dinamicos) y que [Dual Panels Layout](https://github.com/mcventur/DualPaneLayouts).

Como punto de partida se han copiado los fragmentos ListFragment y DetailFragment (clases y layouts) desde el proyecto de Dynamic Fragments. La clase MainActivity se deja con el código por defecto de un nuevo proyecto. 

El componente Jetpack Navigation nos permite navegar entre fragmentos dentro de una actividad. Para utilizarlo necesitamos 3 elementos:
 - Un gráfico de navegación en xml
 - Un NavHost: que será el contenedor de navegación. El elemento sobre el que se van cargando los fragmentos a los que navegamos dinámicamente.
 - NavController: que es el objeto que nos permite efectuar las navegaciones.

## Pasos
1. Añadir las [dependencias para Kotlin del Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started?hl=es-419#Set-up), 
2. Crear el gráfico de navegación: nav_graph.xml. Podéis seguir la [guía oficial](https://developer.android.com/guide/navigation/get-started#create-nav-graph). En él gráfico añadimos los dos fragmentos origen y destino y la transición entre ellos.
3. Crear el NavHost en el activity_main.xml. De nuevo, seguimos la [guía oficial](https://developer.android.com/guide/navigation/get-started#add-navhostfragment). 
El NavHost no deja de ser un FragmentContainerView, que ya conocemos, con 3 atributos específicos:
   ```xml
   <!-- Atributos comunes no mostrados -->
     <androidx.fragment.app.FragmentContainerView
         android:id="@+id/nav_host_fragment"
         android:name="androidx.navigation.fragment.NavHostFragment"
         app:defaultNavHost="true"
         app:navGraph="@navigation/nav_graph"/>
   ```
    - En la propiedad ```android:name``` indicamos la ruta cualificada a la implementación de la clase NavHostFragment`.
    - Con ```app:defaultNavHost="true"``` indicamos que es el NavHost por defecto, importante en paneles duales para definir dónde se reflejará el resultado del botón atrás. 
    - La propiedad ```app:navGraph``` apunta al gráfico de navegación correspondiente que hemos creado en el paso 2. 
4. Sobre el ListFragment, que es el origen de nuestra única transición, tenemos que realizar algunos cambios:
   - Ya no necesitamos esa propiedad lateinit ```private lateinit var starSignListener: StarSignListener```, ni
la llamada al ```onAttach()```, que era donde se inicializaba dicha variable. No necesitamos usar el patrón observer, ya que la Actividad contenedora no va a hacer de "puente" en las transiciones . 
   - en ```onViewCreated()``` sustituimos la asignación del onClickListener en el forEach() por el código que utiliza el componente de Navegación. El código está basado en la guía
     https://developer.android.com/guide/navigation/navigation-navigate?hl=es-419#id
      ```kotlin
          starSigns.forEach {
              //Creamos un Bundle() para el paso de datos, y le añadimos el id del signo pulsado
              val fragmentBundle = Bundle()
              fragmentBundle.putInt(STAR_SIGN_ID, it.id)
          
              //Al hacer click en un signo (it)
              it.setOnClickListener {
                  //Usamos la función navigate de findNavController. Tiene varias versiones sobrecargadas
                  //Esta recibe el id de la acción del gráfico de navegación xml, y, opcionalmente, el Bundle con los datos a pasar
                  //https://developer.android.com/guide/navigation/navigation-navigate?hl=es-419#id
                  findNavController().navigate(R.id.action_listFragment_to_detailFragment, fragmentBundle)
              }
          }
      ```
      Fijaros en cómo usamos el NavController que recuperamos con ```findNavController()``` para lanzar la navegación deseada.
    - Tras esto, ya funcionará la navegación con el paso de datos necesario a través del Bundle. 

Notaréis que no hemos hecho ningún cambio sobre la clase del fragmento de destino ```DetailFragment```, 
que sigue recogiendo los datos como en el ejemplo de Dynamic Fragments:

```kotlin
val starSignId = arguments?.getInt(STAR_SIGN_ID, 0) ?: 0
```

Tenéis esta versión del código disponible en [este commit](https://github.com/mcventur/JetPackFragments/tree/860dc372ad994e7f35b9caad14209d799d1b4e24) del repositorio.

# Uso de SafeArgs de Gradle para navegar con seguridad de tipos
Para pasar datos en nuestra navegación sin usar SafeArgs creamos un objeto Bundle 
en el origen ListFragment con ```fragmentBundle.putInt(STAR_SIGN_ID, it.id)``` y se recoge en el fragmento de destino DetailFragment con 
```val starSignId = arguments?.getInt(STAR_SIGN_ID, 0) ?: 0```

No hay problemas porque se coloca un entero: putInt(), y se recoge un entero: getInt().

Como vemos, debe haber una correspondencia entre el tipo de dato que se pasa y el tipo de dato que se recoge. 

En nuestros proyectos apenas estamos pasando datos y esto es fácil de manejar, pero en proyectos grandes con muchas actividades y fragmentos y un gran volumen de flujo de datos entre ellos, 
será fácil que esto cause problemas.

Para evitarlo, Gradle y Android proponen el uso del plugin Safe Args.

Safe Args, de forma parecida a View Binding, genera clases y métodos para cada acción o transición definida 
en nuestros gráficos de navegación xml. Cada acción será una función estática y recibirá como parámetros fuertemente 
tipados (tipos estrictos, imposible errores en tiempo de ejecución), cada uno de los argumentos recibidos en el destino. 

## Procedimiento de uso de Safe Args

1. **Sobre Gradle:** Lo primero que tenemos que hacer es habilitar el plugin en Gradle para que genere las nuevas clases y poder utilizarlas. 
Podéis seguir la [guía oficial de Safe Args](https://developer.android.com/guide/navigation/use-graph/safe-args?hl=es-419#enable). 

   Ojo: al añadir las líneas al bloque ```plugin{}``` en el gradle a nivel de módulo, como estamos usando SÓLO Kotlin, usad el código indicado para ese caso.   
2. **En el gráfico de navegación nav_graph.xml**, definiremos un argumento starSignId en el destino, indicando que es un entero y con valor por defecto 0:
```xml    
<fragment
   android:id="@+id/starSign"
   android:name="com.mpd.pmdm.jetpackfragments.DetailFragment"
   android:label="fragment_detail"
   tools:layout="@layout/fragment_detail" >
   
   <!-- Definido el argumento -->
   <argument
   android:name="starSignId"
   app:argType="integer"
   android:defaultValue="0" />

</fragment>
```
3. **Sobre el fragmento de origen:** Con el plugin correspondiente habilitado en Gradle, veamos las clases generadas y cómo usarlas:
   - En nuestro caso sólo hay una acción de navegación. Los fragmentos de origen y destino corresponden a las clases siguientes: 
     - origen: ```ListFragment``` 
     - destino: ```DetailFragment```
   - Safe Args genera una clase para cada origen de navegación agrupando todos sus posibles destinos. El nombre de la nueva clase es el de la clase origen añadiendo el sufijo _Directions_. 
   En nuestro ejemplo genera ```ListFragmentDirections```. 

   - Esta clase, contendrá un método estático para cada acción de navegación desde dicho origen. 
   - A su vez, cada uno de estos métodos recibirá los parámetros definidos en el gráfico de navegación xml para el destino.

   Así que ahora, en el onViewCreated del ListFragment, podremos usar la función correspondiente a la acción de navegación de este modo:
   
   ```kotlin
   starSigns.forEach {
      //Al hacer click en un signo (it)
      it.setOnClickListener {
          ListFragmentDirections.actionListFragmentToDetailFragment(starSignId = it.id)
      }
   }
   ```

   Como sólo hay un argumento o parámetro, podríamos habernos ahorrado su nombre en la llamada, pero lo dejo por claridad.

4. **Sobre el fragmento de destino:** Tras enviar los datos desde origen usando SafeArgs, podemos recogerlos en el fragmento de destino de forma muy simple. 
   - Gradle nos genera para cada destino con argumentos una clase con su mismo nombre terminada en _Args_. Es decir, que tendremos
   disponible una clase ```DetailFragmentArgs``` de la que podemos extraer los argumentos. 
   - Usaremos una [delegación de propiedades](https://proandroiddev.com/kotlin-delegates-in-android-1ab0a715762d#:~:text=A%20delegate%20is%20just%20a,letting%20us%20reuse%20this%20logic)
   navArgs() para simplificar la gestión de su get y set, siguiendo el [ejemplo de la guía](https://developer.android.com/guide/navigation/navigation-pass-data?hl=es-419#Safe-args). En la cabecera de DetailFragment:
   ```kotlin
   class DetailFragment : Fragment() {
        val args: DetailFragmentArgs by navArgs()
   ```
   - Tras esto, ya tenemos disponible en ```args``` todos los argumentos recibidos:
   ```kotlin
   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setStarSignData(args.starSignId)
   }
   ```
   
Y con esto, ya estaríamos usando safeArgs para enviar y recibir argumentos en nuestras navegaciones. 



