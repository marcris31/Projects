library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
use IEEE.NUMERIC_STD.ALL;			

entity agenda is
port (clock : in std_logic;
	reset : in std_logic;
	hour_tens : in std_logic_vector(3 downto 0);
	hour_units : in std_logic_vector(3 downto 0);	 
	min_tens : in std_logic_vector(3 downto 0);	 
	min_units : in std_logic_vector(3 downto 0);	
	day_tens : in std_logic_vector(3 downto 0);	 
	day_units : in std_logic_vector(3 downto 0);	
	month_tens : in std_logic_vector(3 downto 0);   
	month_units : in std_logic_vector(3 downto 0);	
	year_1 : in std_logic_vector(3 downto 0);	 
	year_0 : in std_logic_vector(3 downto 0);	  	  
	al_hour_tens : in std_logic_vector(3 downto 0);
	al_hour_units : in std_logic_vector(3 downto 0);	 
	al_min_tens : in std_logic_vector(3 downto 0);	 
	al_min_units : in std_logic_vector(3 downto 0);	 
	al_on : in std_logic;
	temp_1 : in std_logic_vector(3 downto 0);	
	temp_0 : in std_logic_vector(3 downto 0);
	
	hour_tens_out : out std_logic_vector(3 downto 0);
	hour_units_out : out std_logic_vector(3 downto 0); 
	min_tens_out : out std_logic_vector(3 downto 0);	 
	min_units_out : out std_logic_vector(3 downto 0);	
	day_tens_out : out std_logic_vector(3 downto 0);	 
	day_units_out : out std_logic_vector(3 downto 0);	
	month_tens_out : out std_logic_vector(3 downto 0);   
	month_units_out : out std_logic_vector(3 downto 0);	
	year_1_out : out std_logic_vector(3 downto 0);	 
	year_0_out : out std_logic_vector(3 downto 0);  
	day_lett : out string(3 downto 1); 
	alarm : out std_logic;
	temp_1_out : out std_logic_vector(3 downto 0);
	temp_0_out : out std_logic_vector(3 downto 0));
end;

architecture arch_ag of  agenda is 
component alarma is 
	port (
		clk: in std_logic;
		reset: in std_logic;
		alarm_h1: in std_logic_vector(3 downto 0);
		alarm_h0: in std_logic_vector(3 downto 0);
		alarm_m1: in std_logic_vector(3 downto 0);
		alarm_m0: in std_logic_vector(3 downto 0); 
		
		ceas_h1: in std_logic_vector (3 downto 0);
		ceas_h0: in std_logic_vector (3 downto 0);
		ceas_m1: in std_logic_vector (3 downto 0);
		ceas_m0: in std_logic_vector (3 downto 0);
		
		alarm_on: in std_logic;
		alarma_o: OUT std_logic
		
		);
end component;

component calendar_orar is 
	port (
		clk : in std_logic;	
		reset : in std_logic; --when 0, saves data, when 1, changes are made
		minute_1 : in std_logic_vector(3 downto 0);
		minute_0: in std_logic_vector(3 downto 0);
		hour_1 : in std_logic_vector(3 downto 0); 
		hour_0 : in std_logic_vector(3 downto 0);
		day_in_1 : in std_logic_vector(3 downto 0); 
		day_in_0 : in std_logic_vector(3 downto 0);
		month_in_1 : in std_logic_vector(3 downto 0); 
		month_in_0 : in std_logic_vector(3 downto 0);
		year_in_1 : in std_logic_vector(3 downto 0); 
		year_in_0 : in std_logic_vector(3 downto 0);
		
		day_lett: out string (3 downto 1);
		minute_out_1: out std_logic_vector(3 downto 0);
		minute_out_0: out std_logic_vector(3 downto 0);
		hour_out_1:	out std_logic_vector(3 downto 0);
		hour_out_0:	out std_logic_vector(3 downto 0);
		day_out_1 : out std_logic_vector(3 downto 0); 
		day_out_0 : out std_logic_vector(3 downto 0);
		month_out_1 : out std_logic_vector(3 downto 0); 
		month_out_0 : out std_logic_vector(3 downto 0);
		year_out_1 : out std_logic_vector(3 downto 0); 
		year_out_0 : out std_logic_vector(3 downto 0));
end component;

component temp is 
	port 
		(
		temp_1, temp_0: in std_logic_vector(3 downto 0);
		temp_o1, temp_o0: out std_logic_vector(3 downto 0);
		clk: in std_logic;
		reset: in std_logic
		);
end component; 
--signal 
begin 
	alrm: alarma port map (clock, RESET, al_hour_tens, al_hour_units, al_min_tens, al_min_units, hour_tens, hour_units, min_tens, min_units,AL_ON,  alarm );
	data_si_ora: calendar_orar port map (clock, reset, min_tens, min_units, hour_tens, hour_units, day_tens, day_units, month_tens, month_units, year_1, year_0, day_lett, min_tens_out, min_units_out, hour_tens_out, hour_units_out, day_tens_out, day_units_out, month_tens_out, month_units_out, year_1_out, year_0_out );
	temperatura: temp port map (temp_1, temp_0, temp_1_out, temp_0_out, clock, reset);
end;
