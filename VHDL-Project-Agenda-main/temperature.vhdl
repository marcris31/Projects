library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;
use ieee.std_logic_unsigned.all;

entity temp is 
	port 
		(
		temp_1, temp_0: in std_logic_vector(3 downto 0);
		temp_o1, temp_o0: out std_logic_vector(3 downto 0);
		clk: in std_logic;
		reset: in std_logic
		);
end; 

architecture temp of temp is
begin
temp_o1<= temp_1;
temp_o0<= temp_0;
--	signal t1: std_logic_vector(3 downto 0):= "0001";
--	signal t0: std_logic_vector(3 downto 0):= "0110";
--	signal h1: std_logic_vector(3 downto 0):= "0000";
--	signal h0: std_logic_vector(3 downto 0):= "0000";
--	signal m1: std_logic_vector(3 downto 0):= "0000";
--	signal m0: std_logic_vector(3 downto 0):= "0000";
--begin
--	
--	hour_changer: process (clk, reset) 
--	variable l, m: integer;
--	begin 
--		if reset ='1' then 
--			t1<= temp_1;
--			t0<= temp_0;
--		else
--			if rising_edge(clk) then
--				--change minute units
--				m0<= m0 + "0001";
--				--change minute tens
--				if m0= "1001" then
--					m0<= "0000";
--					m1<= m1 + "0001";
--				end if;	
--				-- change hour units
--				if m1 = "0101" and m0 = "1001" then 
--					m1<="0000";
--					m0<= "0000";
--					h0 <=h0+ "0001";
--				END IF;	
--				--change hour tens
--				if 	m1 = "0101" and m0 = "1001" and h0 = "1001" THEN
--					h0<="0000";
--					h1<=h1+"0001";
--				end if;
--				if 	m1 = "0101" and m0 = "1001" and h0 = "0011" and h1 = "0010"	then 
--					h1<="0000";
--					h0<="0000";
--				end if;
--				l:= to_integer(unsigned(h0+h1*"1010"));
--				if l mod 2 = 0 then
--					t0<=t0+"0010";
--					m:= to_integer(unsigned(t0));
--					if m>9 then 
--						t0<="1001";
--					end if;
--					
--					if t0="1001" then
--						t1<= t1+"0001";
--						t0<="0000";
--					end if;	
--				END IF;
--				
--				if l mod 5 = 1 then
--					t0<=t0-"0001";
--					if t0="0000" then
--						t1<= t1-"0001";
--						t0<="1001";
--					end if;
--				END IF;	
--				
--				IF l<= 20 and l >= 13 then
--					t1<=t1;
--					t0<=t0;
--				end if;
--			end if;
--		end if;
--	end process;
--	
--	temp_o1<=t1;
--	temp_o0<=t0;
--	
--	
end;

