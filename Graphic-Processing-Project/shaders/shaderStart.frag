#version 410 core

in vec3 fNormal;
in vec4 fPosEye;
in vec2 fTexCoords;
in vec4 fragPosLightSpace;

out vec4 fColor;

//lighting
uniform	vec3 lightDir;
uniform	vec3 lightColor;

//texture
uniform sampler2D diffuseTexture;
uniform sampler2D specularTexture;
uniform sampler2D shadowMap;

//fog
uniform int fogReady;
uniform float fogDensity;

//pointlight
uniform int startPoint;
uniform vec3 lightPos1;
uniform mat4 view;

//night
uniform int night;



vec3 ambient;
float ambientStrength = 0.2f;
vec3 diffuse;
vec3 specular;
float specularStrength = 0.5f;
float shininess = 32.0f;




void computeLightComponents()
{		
	vec3 cameraPosEye = vec3(0.0f);//in eye coordinates, the viewer is situated at the origin
	
	//transform normal
	vec3 normalEye = normalize(fNormal);	
	
	//compute light direction
	vec3 lightDirN = normalize(lightDir);
	
	//compute view direction 
	vec3 viewDirN = normalize(cameraPosEye - fPosEye.xyz);
		
	//compute ambient light
	ambient = ambientStrength * lightColor;
	
	//compute diffuse light
	diffuse = max(dot(normalEye, lightDirN), 0.0f) * lightColor;
	
	//compute specular light
	vec3 reflection = reflect(-lightDirN, normalEye);
	float specCoeff = pow(max(dot(viewDirN, reflection), 0.0f), shininess);
	specular = specularStrength * specCoeff * lightColor;
}

//fog
float computeFog()
{
	float fogDensity = 0.004f;
	float fragmentDistance = length(fPosEye);
	float fogFactor = exp(-pow(fragmentDistance * fogDensity, 2));
	return clamp(fogFactor, 0.0f, 1.0f);
}
//shadows
float computeShadow()
{
	// perform perspective divide
	vec3 normalizedCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;

	// Transform to [0,1] range
	normalizedCoords = normalizedCoords * 0.5 + 0.5;

	// Get closest depth value from light's perspective
	float closestDepth = texture(shadowMap, normalizedCoords.xy).r;

	// Get depth of current fragment from light's perspective
	float currentDepth = normalizedCoords.z;

	// Check whether current frag pos is in shadow
	float bias = 0.005f;
    float shadow = currentDepth - bias> closestDepth  ? 1.0f : 0.0f;

	return shadow;
}

float constant = 2.0f;
float linear = 0.00545f;
float quadratic = 0.0035f;

//point light
vec3 computePointLight(vec4 lightPosEye)
{
float ambientStrength1 = 0.0001f;
float specularStrength1 = 0.0001f;
float shininess1 = 1.0f;

	vec3 cameraPosEye = vec3(0.0f);
	vec3 normalEye = normalize(fNormal);
	//compute light direction
	vec3 lightDirN = normalize(lightPosEye.xyz - fPosEye.xyz);
	
	//compute distance to light
	float dist = length(lightPosEye.xyz - fPosEye.xyz);
	
	//compute attenuation
	
	float a = 3.0;
	float b = 4.0;
	//float att = 1.0f / (constant + linear * dist + quadratic * (dist * dist));
	float att=1.0f/(a * dist * dist + b * dist + 1.0f);
	
	//compute ambient light
	ambient = att * ambientStrength1 * lightColor;
	vec3 viewDirN = normalize(cameraPosEye - fPosEye.xyz);
	vec3 halfVector = normalize(lightDirN + viewDirN);

	float specCoeff = pow(max(dot(normalEye, halfVector), 0.0f), shininess1);
	//compute diffuse light
	diffuse = att * max(dot(normalEye, lightDirN), 0.0f) * lightColor;
	specular = att * specularStrength1 * specCoeff * lightColor;
	return (ambient + diffuse+specular)*att*vec3(2.0f,2.0f,2.0f);
}

void main() 
{
	computeLightComponents();
	
	vec3 baseColor = vec3(0.9f, 0.35f, 0.0f);//orange
	
	ambient *= texture(diffuseTexture, fTexCoords).rgb;
	diffuse *= texture(diffuseTexture, fTexCoords).rgb;
	specular *= texture(specularTexture, fTexCoords).rgb;

	

    //fog
	float fogFactor=computeFog();
	vec4 fogColor = vec4(0.5f, 0.5f, 0.5f, 1.0f);

	float shadow = computeShadow();
	//vec3 color = min((ambient + diffuse) + specular, 1.0f);
	// modulate with shadow
	vec3 color = min((ambient+(1.0f-shadow)*diffuse)+(1.0f-shadow)* specular,1.0f);
    
	vec4 colorWithShadow = vec4(color,1.0f);
	
	if(startPoint==1)
	{
		vec4 lightPosEye1 = view * vec4(lightPos1, 1.0f);
		color+=computePointLight(lightPosEye1);
	}
	if(fogReady==1){

		fColor = mix(fogColor, min(colorWithShadow*vec4(color, 1.0f),1.0f), fogFactor);

	}else{

		 //fColor = min(colorWithShadow*vec4(color, 1.0f),1.0f);
		 fColor=vec4(color, 1.0f);
	}

	if (night == 1) {
	fColor = vec4(0.0f, 0.0f, 0.0f, 1.0f);
	}

	
}
