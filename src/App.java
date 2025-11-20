
/**
 * @version 1.2 - 5 novembre 2024
 * @author Michel Buffa + amélioration de Philippe Lahire
 *         Inspire par la classe Reflectiontest.java de
 *         Cay S. Horstmann & Gary Cornell, publiee dans le livre Core Java, Sun
 *         Press
 */
import java.lang.reflect.*;

public class App {

  public static void analyseClasse(String nomClasse, String decalage) throws ClassNotFoundException {
    // Récupération d'un objet de type Class correspondant au nom passé en paramètre
    // et analyse en plusieurs étapes

    Class<?> cl = getClasse(nomClasse);

    affichePackageDeLaClasse(cl, decalage);

    afficheEnTeteClasse(cl, decalage);

    System.out.println(decalage + " ");
    afficheInnerClasses(cl, decalage);

    System.out.println(decalage + " ");
    afficheAttributs(cl, decalage);

    System.out.println("\n" + decalage + " ");
    afficheConstructeurs(cl, decalage);

    System.out.println(decalage + "\n" + decalage + " ");
    afficheMethodes(cl, decalage);

    // L'accolade fermante de fin de classe !
    System.out.println(decalage + "}");
  }

  public static Class<?> getClasse(String nomClasse) throws ClassNotFoundException {
    // Retourne la classe dont le nom est passé en paramètre
    return Class.forName(nomClasse);
  }

  public static void affichePackageDeLaClasse(Class<?> cl, String decalage) {
    // Affiche le package de la classe : package....
    System.out.println(decalage + "package " + cl.getPackage().getName() + ";");
  }

  public static void afficheEnTeteClasse(Class<?> cl, String decalage) {
    // Affiche l'entête d'une classe
    // Cette méthode affiche par ex "public class Toto extends Tata implements Titi,
    // Tutu {"

    // Code à écrire :
    // Affichage du modifier et du nom de la classe
    System.out.print(decalage + Modifier.toString(cl.getModifiers()) + " class " + cl.getSimpleName());
    // Récupération de la superclasse si elle existe (null si cl est le type Object)
    Class<?> superCl = cl.getSuperclass() != null ? cl.getSuperclass() : null;
    // On ecrit le "extends " que si la superclasse est non nulle et différente de
    // Object
    if (superCl != null && superCl != Object.class) {
      System.out.println(decalage + "extends " + superCl.getSimpleName() + " {");
    }
    // Affichage des interfaces que la classe implemente (affichage de "implements"
    // s'il y a des interfaces à implémenter)
    Class<?>[] interfaces = cl.getInterfaces();

    if (interfaces.length > 0) {
      System.out.print(decalage + "implements ");
      for (int i = 0; i < interfaces.length; i++) {
        System.out.print(interfaces[i].getSimpleName());
        if (i < interfaces.length - 1) {
          System.out.print(", ");
        }
      }
      System.out.println(" {");
    }
    // Accolade ouvrante du début de la classe !
    System.out.println(decalage + "{");
  }

  public static void afficheInnerClasses(Class<?> cl, String decalage) throws ClassNotFoundException {
    // Affiche les classes imbriquées statiques ou pas (à faire après avoir fait
    // fonctionner le reste)
    // Code à écrire
    Class<?>[] innerClasses = cl.getDeclaredClasses();

    for (Class<?> innerClass : innerClasses) {
      analyseClasse(innerClass.getName(), decalage + " ");
    }
  }

  public static void afficheAttributs(Class<?> cl, String decalage) {
    // Affichage des attributs de la classe : utilise afficheTypeBasic ou
    // afficheTypeFull
    Field[] fields = cl.getDeclaredFields();
    for (Field field : fields) {
      System.out.print(decalage + Modifier.toString(field.getModifiers()) + " ");
      afficheTypeBasic(field.getType(), decalage);
      System.out.println(" " + field.getName() + ";");
    }
  }

  private static void afficheTypeBasic(Class<?> cl, String decalage) {
    // Affichage du type de l'attribut : Ne prend pas en compte l'instanciation de
    // types génériques
    System.out.print(cl.getSimpleName());
  }

  private static void afficheTypeFull(Class<?> t, Type gt, String decalage) {
    // Affichage du type de l'attribut : Prend en compte l'instanciation de types
    // génériques
    if (gt instanceof ParameterizedType) {
      ParameterizedType pType = (ParameterizedType) gt;
      System.out.print(((Class<?>) pType.getRawType()).getSimpleName() + "<");
      Type[] typeArgs = pType.getActualTypeArguments();
      for (int i = 0; i < typeArgs.length; i++) {
        if (typeArgs[i] instanceof Class<?>) {
          System.out.print(((Class<?>) typeArgs[i]).getSimpleName());
        } else {
          System.out.print(typeArgs[i].getTypeName());
        }
        if (i < typeArgs.length - 1) {
          System.out.print(", ");
        }
      }
      System.out.print(">");
    } else {
      System.out.print(t.getTypeName());
    }
  }

  public static void afficheConstructeurs(Class<?> cl, String decalage) {
    // Affichage des constructeurs de la classe : utilise afficheParamsBasic ou
    // afficheParamsFull
    Constructor<?>[] constructors = cl.getDeclaredConstructors();
    for (Constructor<?> constructor : constructors) {
      System.out.print(decalage + Modifier.toString(constructor.getModifiers()) + " " + constructor.getName() + "(");
      Class<?>[] paramTypes = constructor.getParameterTypes();
      Type[] genericParamTypes = constructor.getGenericParameterTypes();
      afficheParamsFull(paramTypes, genericParamTypes, decalage);
      System.out.println(");");
    }
  }

  public static void afficheMethodes(Class<?> cl, String decalage) {
    // Affichage des méthodes de la classe : utilise afficheParamsBasic ou
    // afficheParamsFull
    Method[] methods = cl.getDeclaredMethods();
    for (Method method : methods) {
      System.out.print(decalage + Modifier.toString(method.getModifiers()) + " ");
      afficheTypeBasic(method.getReturnType(), decalage);
      System.out.print(" " + method.getName() + "(");
      Class<?>[] paramTypes = method.getParameterTypes();
      Type[] genericParamTypes = method.getGenericParameterTypes();
      afficheParamsFull(paramTypes, genericParamTypes, decalage);
      System.out.println(");");
    }
  }

  private static void afficheParamsBasic(Class<?>[] params, String decalage) {
    // Affichage du type de chaque paramètre : Ne prend pas en compte
    // l'instanciation de types génériques
    for (int i = 0; i < params.length; i++) {
      System.out.print(params[i].getSimpleName());
      if (i < params.length - 1) {
        System.out.print(", ");
      }
    }
  }

  private static void afficheParamsFull(Class<?>[] params, Type[] gparams, String decalage) {
    // Affichage du type de chaque paramètre : Prend en compte l'instanciation de
    // types génériques
    for (int i = 0; i < params.length; i++) {
      afficheTypeFull(params[i], gparams[i], decalage);
      if (i < params.length - 1) {
        System.out.print(", ");
      }
    }
  }

  public static void main(String[] args) {
    // Récupérer le nom de la classe dans 'args' avec valeur par défaut
    String classe = (args.length >= 1 && args[0] != null && !args[0].isBlank())
        ? args[0]
        : "java.util.HashMap";
    try {
      analyseClasse(classe, "");
    } catch (ClassNotFoundException e) {
      System.out.println("Classe " + classe + " non trouvée.");
    }
  }
}