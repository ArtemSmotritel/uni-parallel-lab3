with Ada.Text_IO, GNAT.Semaphores;
use Ada.Text_IO, GNAT.Semaphores;

with Ada.Containers.Indefinite_Doubly_Linked_Lists;
use Ada.Containers;

procedure Lab3 is
   package String_Lists is new Indefinite_Doubly_Linked_Lists (String);
   use String_Lists;

   procedure Starter (Storage_Size : in Integer; Producer_Count : in Integer; Consumer_Count : in Integer; Items_Per_Producer : in Integer; Items_Per_Consumer : in Integer) is
      Storage : List;

      Access_Storage : Counting_Semaphore (1, Default_Ceiling);
      Full_Storage   : Counting_Semaphore (Storage_Size, Default_Ceiling);
      Empty_Storage  : Counting_Semaphore (0, Default_Ceiling);

      task type Producer is
         entry Start(Item_Numbers : in Integer; Id : in Integer);
      end Producer;

      task type Consumer is
         entry Start(Item_Numbers : in Integer; Id : in Integer);
      end Consumer;

      task body Producer is
         Item_Numbers : Integer;
         Id : Integer;
      begin
         accept Start (Item_Numbers : in Integer; Id : in Integer) do
            Producer.Item_Numbers := Item_Numbers;
            Producer.Id := Id;
         end Start;

         for i in 1 .. Item_Numbers loop
            Full_Storage.Seize;
            Access_Storage.Seize;

            Storage.Append ("item " & i'Img & "; P" & Id'Img);
            Put_Line ("Added item " & i'Img);

            Access_Storage.Release;
            Empty_Storage.Release;
            delay 2.0;
         end loop;

      end Producer;

      task body Consumer is
         Item_Numbers : Integer;
         Id : Integer;
      begin
         accept Start (Item_Numbers : in Integer; Id : in Integer) do
            Consumer.Item_Numbers := Item_Numbers;
            Consumer.Id := Id;
         end Start;

         for i in 1 .. Item_Numbers loop
            Empty_Storage.Seize;
            Access_Storage.Seize;

            declare
               item : String := First_Element (Storage);
            begin
               Put_Line ("Took " & item & ". From Consumer " & Id'Img);
            end;

            Storage.Delete_First;

            Access_Storage.Release;
            Full_Storage.Release;

            delay 1.0;
         end loop;

      end Consumer;

      Producers : array(1..Producer_Count) of Producer;
      Consumers : array(1..Consumer_Count) of Consumer;

   begin
      for I in Producers'Range loop
         Producers(I).Start(Item_Numbers => Items_Per_Producer, Id => I);
      end loop;

      for I in Consumers'Range loop
         Consumers(I).Start(Item_Numbers => Items_Per_Consumer, Id => I);
      end loop;

   end Starter;

begin
   Starter (Storage_Size       => 5,
            Producer_Count     => 3,
            Consumer_Count     => 2,
            Items_Per_Producer => 6,
            Items_Per_Consumer => 9);
   null;
end Lab3;
