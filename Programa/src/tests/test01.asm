.data
newLine: .asciiz "\n"
.text
.globl main
main:
j global
finalCodigo:
li $v0, 10
syscall
