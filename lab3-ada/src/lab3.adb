with Ada.Text_IO, GNAT.Semaphores;
use Ada.Text_IO, GNAT.Semaphores;

with Ada.Containers.Indefinite_Doubly_Linked_Lists; use Ada.Containers;

procedure Lab3 is
   package String_Lists is new Indefinite_Doubly_Linked_Lists (String);
   use String_Lists;

   procedure Starter
     (Storage_Size       : in Integer; Producer_Count : in Integer;
      Consumer_Count     : in Integer; Items_Per_Producer : in Integer;
      Items_Per_Consumer : in Integer)
   is
      Storage : List;

      Access_Storage : Counting_Semaphore (1, Default_Ceiling);
      Full_Storage   : Counting_Semaphore (Storage_Size, Default_Ceiling);
      Empty_Storage  : Counting_Semaphore (0, Default_Ceiling);

      task type Producer is
         entry Start
           (Item_Numbers : in Integer; Id : in Integer;
            Is_Dummy     : in Boolean := False);
      end Producer;

      task type Consumer is
         entry Start
           (Item_Numbers : in Integer; Id : in Integer;
            Is_Dummy     : in Boolean := False);
      end Consumer;

      task body Producer is
         Item_Numbers : Integer;
         Id           : Integer;
         Is_Dummy     : Boolean;
      begin
         accept Start
           (Item_Numbers : in Integer; Id : in Integer;
            Is_Dummy     : in Boolean := False)
         do
            Producer.Item_Numbers := Item_Numbers;
            Producer.Id           := Id;
            Producer.Is_Dummy     := Is_Dummy;
         end Start;

         for i in 1 .. Item_Numbers loop
            Full_Storage.Seize;
            Access_Storage.Seize;

            if Is_Dummy then
               Storage.Append ("DUMMY item " & i'Img & "; P" & Id'Img);
               Put_Line
                 (File => Standard_Error,
                  Item => "Added a DUMMY item " & i'Img);
            else
               Storage.Append ("item " & i'Img & "; P" & Id'Img);
               Put_Line ("Added an item " & i'Img);
            end if;

            Access_Storage.Release;
            Empty_Storage.Release;
            delay 2.0;
         end loop;

      end Producer;

      task body Consumer is
         Item_Numbers : Integer;
         Id           : Integer;
         Is_Dummy     : Boolean;

         Output_Stream : File_Type;
      begin
         accept Start
           (Item_Numbers : in Integer; Id : in Integer;
            Is_Dummy     :    Boolean := False)
         do
            Consumer.Item_Numbers := Item_Numbers;
            Consumer.Id           := Id;
            Consumer.Is_Dummy     := Is_Dummy;
         end Start;

         for i in 1 .. Item_Numbers loop
            Empty_Storage.Seize;
            Access_Storage.Seize;

            declare
               item : String := First_Element (Storage);
            begin
               if Is_Dummy then
                  Put_Line
                    (File => Standard_Error,
                     Item => "A DUMMY Took " & item & ". Consumer" & Id'Img);
               else
                  Put_Line ("Took " & item & ". Consumer" & Id'Img);
               end if;
            end;

            Storage.Delete_First;

            Access_Storage.Release;
            Full_Storage.Release;

            delay 1.0;
         end loop;

      end Consumer;

      Producers      : array (1 .. Producer_Count) of Producer;
      Consumers      : array (1 .. Consumer_Count) of Consumer;
      Items_Produced : Integer;
      Items_Consumed : Integer;

   begin
      for I in Producers'Range loop
         Producers (I).Start (Item_Numbers => Items_Per_Producer, Id => I);
      end loop;

      for I in Consumers'Range loop
         Consumers (I).Start (Item_Numbers => Items_Per_Consumer, Id => I);
      end loop;

      Items_Consumed := Items_Per_Consumer * Consumer_Count;
      Items_Produced := Items_Per_Producer * Producer_Count;

      if Items_Produced = Items_Consumed then
         return;
      elsif Items_Produced > Items_Consumed then
         declare
            Dummy_Consummer : Consumer;
         begin
            Dummy_Consummer.Start
              (Item_Numbers => Items_Produced - Items_Consumed,
               Id           => Consumers'Length + 1, Is_Dummy => True);
         end;
      else
         declare
            Dummy_Producer : Producer;
         begin
            Dummy_Producer.Start
              (Item_Numbers => Items_Consumed - Items_Produced,
               Id           => Producers'Length + 1, Is_Dummy => True);
         end;
      end if;
   end Starter;

begin
   Starter
     (Storage_Size       => 1, Producer_Count => 4, Consumer_Count => 8,
      Items_Per_Producer => 2, Items_Per_Consumer => 1);
end Lab3;
