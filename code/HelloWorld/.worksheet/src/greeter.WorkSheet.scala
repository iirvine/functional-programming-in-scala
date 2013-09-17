package greeter

object WorkSheet {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(79); 
  println("Welcome to the Scala worksheet");$skip(12); 
  val x = 5;System.out.println("""x  : Int = """ + $show(x ));$skip(16); 
  val i = x * x;System.out.println("""i  : Int = """ + $show(i ))}
}
