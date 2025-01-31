.data
newLine: .asciiz "\n"
string0: .asciiz " + "
string1: .asciiz " = "
string2: .asciiz "hola mundo"
.text
.globl main
main:
j global
func1:
subu $sp, $sp, 4
sw $ra, 0($sp)
addu $sp, $sp, 4
lw $ra, 0($sp)
jr $ra
global:
subu $sp, $sp, 12
sw $ra, 0($sp)
li $s7, 5
sw $s7, 4($sp)
li $s7, 3
sw $s7, 8($sp)
lw $t0, 4($sp)
move $a0, $t0
li $v0, 1
syscall
la $a0,string0
li $v0, 4
syscall
lw $t1, 8($sp)
move $a0, $t1
li $v0, 1
syscall
la $a0,string1
li $v0, 4
syscall
lw $t2, 4($sp)
lw $t3, 8($sp)
add $t4, $t2, $t3
move $a0, $t4
li $v0, 1
syscall
li $v0, 4
la $a0, newLine
syscall
li.s $f12,3.14
li $v0, 2
syscall
li $a0, 3
li $v0, 1
syscall
la $a0,string2
li $v0, 4
syscall
li $v0, 4
la $a0, newLine
syscall
addu $sp, $sp, 12
j finalCodigo
finalCodigo:
li $v0, 10
syscall
