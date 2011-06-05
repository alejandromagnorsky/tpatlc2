Para compilar la aplicación: 
sh ./compile

Para ejecutar la aplicación compilada:
java -jar TuringMachine.jar [ -g ] <source>

Para generar la imagen del autómata en base al archivo .dot:
dot -Tpng -o automata.png automata.dot