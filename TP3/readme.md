# TP03 - Jeux des Allumettes

Dans ce TP vous allez apprendre à faire une `View`, et à faire une synchronisation entre plusieurs `Thread` dans une application Android.

## Le jeu

Le jeu des allumettes est simple à deux joueurs. Il y a n allumettes au départ. A tour de rôle, chaque joueur va prendre, au choix, 1, 2 ou 3 allumettes. Celui qui prend la dernière allumette perd. 

## 1. Créer le projet

Créer un nouveau projet sur Android Studio.

* Choisir le template "Phone and Tablet" / "No Activity"
* Name: Allumettes, Package name : `fr.unice.l3.allumettes`
* Minimum SDK: API 21 Android 5.0

## 2. Faire la `View` pour afficher les allumettes

[Image de l'UI finale]

Il s’agit de créer une `View`, c'est à dire un élément graphique réutilisable personnalisé qui permet d’afficher les allumettes comme illustré ci-dessus. Une fois cette `View` créée, nous pourrons l'insérer dans le layout de notre `Activity` de jeu, tout comme les éléments d'interface disponibles par défaut (ex : Switch, ScrollView, etc).

### 2.1. Mise en place

* Créez une classe `Allumettes.java` héritant de `View` (dans un package `fr.unice.l3.allumettes.view`).
  * Surchargez le constructeur (obligatoire pour surcharger `View`) :
  ```java
  public Allumettes(Context context, @Nullable AttributeSet attrs) {
  super(context, attrs);
  // on complétera ensuite...
  }
	```
  * La classe `Allumettes` va contenir des informations sur l'état actuel du "plateau de jeu". Par exemple vous pouvez faire cela en ajoutant en attributs de cette classe :
    * Le nombre total d'allumettes du jeu (valeur par défaut = 21)
    * Le nombre d'allumettes visibles (valeur par défaut = 21)
    * Le nombre d'allumettes sélectionnées (valeur par défaut = 0)

La `View` se chargera d'agencer à l'écran des allumettes. D'abord on définit le design d'une seule allumette dans un `Drawable` .

* Créez dans votre projet ce fichier `res/drawable/allumette.xml`  dont on fournit un exemple de code (vous pouvez modifier les couleurs ou proportions si souhaité) :

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item>
        <shape android:shape="rectangle">
            <solid android:color="@android:color/holo_red_dark" />
        </shape>
    </item>
    <item android:top="10dp">
        <shape android:shape="rectangle"  >
            <solid android:color="@android:color/holo_orange_light" />
        </shape>
    </item>
    <item >
        <shape android:shape="rectangle"  >
            <stroke android:color="@android:color/black"
                android:width="1dp"/>
        </shape>
    </item>
</layer-list>
```

* Créez une `Activity` (empty activity) `GameActivity` que vous ne modifiez pas pour le moment.

  * Dans `AndroidManifest.xml` déclarez cette `Activity` comme étant l'activité par défaut de cette app :

   ```xml
    ...
    <activity
    	android:name=".GameActivity"
      android:exported="true">
      <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
    </activity>
    ...
   ```

  * Dans le `layout` associé à cette activité (`res/layout/activity_game.xml`), insérez en plein écran la `View`, grâce au code suivant par example :

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".GameActivity">

    <fr.unice.l3.allumettes.view.Allumettes
        android:id="@+id/allumettes"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

* Le layout `activity_game.xml` vous servira à tester le rendu visuel de votre `View`, dans la vue "Design". Passez l'orientation du smartphone en format "Landscape" (mais à terme il faudra tester que l'affichage soit satisfaisant dans les 2 orientations).  Pour le moment seul un écran blanc s'affiche.

### 2.2. Dessin des allumettes

* Nous allons maintenant surcharger la méthode `void onDraw(Canvas canvas)` dans la classe `Allumettes` pour dessiner les allumettes.

  * **Affichage naïf** : Considérons d'abord qu'on choisit une **taille fixe** pour chaque allumette (ex : largeur = 30, hauteur = 300)]

    * Récupérez le `Drawable` qui représente une allumette dans le constructeur de `Allumettes`:

     ```java
      public class Allumettes extends View {
        ...
        private Drawable allumette; // stocker la référence au Drawable comme attribut de la classe
        
        public Allumettes(Context context, @Nullable AttributeSet attrs) {
          super(context, attrs);
          allumette = context.getDrawable(R.drawable.allumette);
        }
      }
     ```

    * Surchargez la méthode `void onDraw(Canvas canvas)` pour dessiner les 21 allumettes du jeu. Pour dessiner une allumette on fait :

     ```java
      allumette.setBounds(positionBordGauche, positionBordHaut, positionBordDroit, positionBordBas);
      allumette.draw(canvas)
     ```

    * Ce premier **affichage naïf** des allumettes doit ressembler à peu près à ça : ![image-ui01](tp03-ui-01.png)
    * Notez qu'on a :
      * De l'espace sur les bords verticaux et horizontaux de l'écran (alors que la `View` `allumettes` prend bien toute la place sur l'écran dans le layout)
      * Un espacement égal à la largeur d'une allumette entre 2 allumettes

  * Ce premier affichage n'est pas vraiment satisfaisant, les allumettes ne prennent pas toute la place disponible sur l'écran, et si l'écran était trop étroit elles dépasseraient. Maintenant faites un **affichage responsive** des allumettes, telles que leur largeur et hauteur s'adapte à la taille de l'écran.

    * Surchargez la méthode `void onSizeChanged(int w, int h, int oldw, int oldh)` pour calculer des nouvelles dimensions pour les allumettes selon la taille de l'écran. Cela peut se faire en définissant des attributs `allumetteLargeur` et `allumetteHauteur` sur la classe `Allumettes` et en calculant ces derniers en fonction de la largeur et hauteur de l'écran (`w `et `h` ou accessibles par les méthodes `getWidth()` et `getHeight()`).
    * Le résultat :![image-ui02](tp03-ui-02.png)
    * Ces allumettes ne sont pas très esthétiques car elles sont trop étirées (et le résultat est pire en mode portrait...). Définissez un ratio $\frac{largeur}{hauteur}$ minimal et maximal pour éviter des étirements extrêmes. Par exemple : $0.1 \leq \frac{largeur}{hauteur} \leq 0.2$. Alors, si le ratio est trop bas on diminue la hauteur, et s'il est trop élevé on diminue la largeur de l'allumette pour que le ratio soit dans les bornes.![image-ui03](tp03-ui-03.png)
    * Bonus : centrez les allumettes verticalement sur l'écran ![image-ui04](tp03-ui-04.png)
    * Bonus : ajoutez la possibilité d'avoir plusieurs lignes d'allumettes, par exemple 2 lignes.

### 2.3. Indication de l'état des allumettes (sélectionnée et enlevée)

* Maintenant nous allons ajouter la possibilité d'afficher un état du jeu où un certain nombre d'allumettes (1, 2 ou 3) sont sélectionnées par un joueur.
  * Ajoutez à la classe `Allumettes` un getter/setter afin qu'un utilisateur de cette classe puisse modifier l'attribut indiquant le nombre d'allumettes sélectionnées.
  * Nous représenterons le fait qu'une allumette est sélectionnée en l'entourant d'un rectangle vert épais [image]


