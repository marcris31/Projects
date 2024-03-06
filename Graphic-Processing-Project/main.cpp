//
//  main.cpp
//  OpenGL Advances Lighting
//
//  Created by CGIS on 28/11/16.
//  Copyright � 2016 CGIS. All rights reserved.
//

#if defined (__APPLE__)
#define GLFW_INCLUDE_GLCOREARB
#define GL_SILENCE_DEPRECATION
#else
#define GLEW_STATIC
#include <GL/glew.h>
#endif

#include <GLFW/glfw3.h>

#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/matrix_inverse.hpp>
#include <glm/gtc/type_ptr.hpp>

#include "Shader.hpp"
#include "Model3D.hpp"
#include "Camera.hpp"

#include <iostream>
#include "Skybox.hpp"

//window size
int glWindowWidth = 1500;
int glWindowHeight = 1000;
int retina_width, retina_height;
float fov = 45.0f;
GLFWwindow* glWindow = NULL;

//matrices
glm::mat4 model;
GLuint modelLoc;
glm::mat4 view;
GLuint viewLoc;
glm::mat4 projection;
GLuint projectionLoc;
glm::mat3 normalMatrix;
GLuint normalMatrixLoc;
glm::mat4 lightRotation;

//light param
glm::vec3 lightDir;
GLuint lightDirLoc;
glm::vec3 lightColor;
GLuint lightColorLoc;

//camera
gps::Camera myCamera(
	glm::vec3(0.0f, 2.0f, 5.5f),
	glm::vec3(0.0f, 0.0f, 0.0f),
	glm::vec3(0.0f, 1.0f, 0.0f));
//viteza camerei
float cameraSpeed = 1.0f;

bool pressedKeys[1024];
GLfloat angleY = 0.0f; //angle
GLfloat lightAngle;

//my models
gps::Model3D myModel;
gps::Model3D screenQuad;
gps::Model3D lightCube;
gps::Model3D carModel;
gps::Model3D turbineModel;

//shaders
gps::Shader myCustomShader;
gps::Shader lightShader;
gps::Shader screenQuadShader;
gps::Shader depthMapShader;
gps::Shader skyboxShader;

//fog
int fogReady;
GLint foginitLoc;
int foginit = 0;
GLfloat fogDensity = 0.0005f;
GLint fogDensityLoc;

//shadows
GLuint shadowMapFBO;
GLuint depthMapTexture;
const unsigned int SHADOW_WIDTH = 2048 * 9;
const unsigned int SHADOW_HEIGHT = 2048 * 9;

//point light 
int startPoint = 0;
glm::vec3 lightPos1;
GLuint lightPos1Loc;

//skybox
gps::SkyBox mySkyBox;

//night mode
GLint isNightLoc;
GLint nightLightColorLoc;
glm::vec3 nightLightColor = glm::vec3(0.04f, 0.04f, 0.04f);
int night = 0;



float alpha = 0.0f;

bool showDepthMap;

GLenum glCheckError_(const char* file, int line) {
	GLenum errorCode;
	while ((errorCode = glGetError()) != GL_NO_ERROR)
	{
		std::string error;
		switch (errorCode)
		{
		case GL_INVALID_ENUM:                  error = "INVALID_ENUM"; break;
		case GL_INVALID_VALUE:                 error = "INVALID_VALUE"; break;
		case GL_INVALID_OPERATION:             error = "INVALID_OPERATION"; break;
		case GL_OUT_OF_MEMORY:                 error = "OUT_OF_MEMORY"; break;
		case GL_INVALID_FRAMEBUFFER_OPERATION: error = "INVALID_FRAMEBUFFER_OPERATION"; break;
		}
		std::cout << error << " | " << file << " (" << line << ")" << std::endl;
	}
	return errorCode;
}
#define glCheckError() glCheckError_(__FILE__, __LINE__)

void windowResizeCallback(GLFWwindow* window, int width, int height) {
	fprintf(stdout, "window resized to width: %d , and height: %d\n", width, height);
	//TODO	
}

void keyboardCallback(GLFWwindow* window, int key, int scancode, int action, int mode) {
	if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
		glfwSetWindowShouldClose(window, GL_TRUE);

	if (key == GLFW_KEY_M && action == GLFW_PRESS)
		showDepthMap = !showDepthMap;

	if (key >= 0 && key < 1024)
	{
		if (action == GLFW_PRESS)
			pressedKeys[key] = true;
		else if (action == GLFW_RELEASE)
			pressedKeys[key] = false;
	}
}

bool firstMouse = true;
float lastX = 800.0f / 2.0;
float lastY = 600.0 / 2.0;
float yaw = -90.0f;	// yaw is initialized to -90.0 degrees since a yaw of 0.0 results in a direction vector pointing to the right so we initially rotate a bit to the left.
float pitch = 0.0f;
void mouseCallback(GLFWwindow* window, double xpos, double ypos) {
	if (firstMouse)
	{
		firstMouse = false;
		lastX = xpos;
		lastY = ypos;
	}

	float x_offset = xpos - lastX;
	float y_offset = lastY - ypos;

	lastX = xpos;
	lastY = ypos;

	float sensitivity = 0.1f;
	x_offset *= sensitivity;
	y_offset *= sensitivity;

	yaw += x_offset;
	pitch += y_offset;

	if (pitch > 89.0f)
		pitch = 89.0f;
	if (pitch < -89.0f)
		pitch = -89.0f;

	myCamera.rotate(pitch, yaw);
}

//scrollCallback function
void scrollCallback(GLFWwindow* window, double xoffset, double yoffset) {
	fov -= (float)yoffset;
	if (fov < 1.0f)
		fov = 1.0f;
	if (fov > 45.0f)
		fov = 45.0f;
}


float angle = 0;
float angle1 = 0;

void turbineRotation() {
	alpha += 1.0f;
	if (alpha >= 360.0f) {
		alpha -= 360.0f;
	}
}

void processMovement()
{
	// I= initial state
	if (glfwGetKey(glWindow, GLFW_KEY_I)) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}
	
	// U= wireframe state
	if (glfwGetKey(glWindow, GLFW_KEY_U)) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); 
	}
	
	// V= point state, vertices
	if (glfwGetKey(glWindow, GLFW_KEY_V)) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_POINT); 
	}
	
	///Z= smooth view
	if (glfwGetKey(glWindow, GLFW_KEY_Z)) {
		glEnable(GL_MULTISAMPLE);
	}
	
	//rotate light to the left
	if (pressedKeys[GLFW_KEY_Q]) {
		angleY -= 1.0f;
	}

	//rotate light to the right
	if (pressedKeys[GLFW_KEY_E]) {
		angleY += 1.0f;
	}
	
	//rotate shadow light to the left
	if (pressedKeys[GLFW_KEY_J]) {
		lightAngle -= 1.0f;
	}

	//rotate shadow light to the right
	if (pressedKeys[GLFW_KEY_L]) {
		lightAngle += 1.0f;
	}

	//move camera forward
	if (pressedKeys[GLFW_KEY_W]) {
		myCamera.move(gps::MOVE_FORWARD, cameraSpeed);
	}

	//move camera backward
	if (pressedKeys[GLFW_KEY_S]) {
		myCamera.move(gps::MOVE_BACKWARD, cameraSpeed);
	}

	//move camera left
	if (pressedKeys[GLFW_KEY_A]) {
		myCamera.move(gps::MOVE_LEFT, cameraSpeed);
	}

	//move camera right
	if (pressedKeys[GLFW_KEY_D]) {
		myCamera.move(gps::MOVE_RIGHT, cameraSpeed);
	}

	//start fog
	if (pressedKeys[GLFW_KEY_F]) {
		myCustomShader.useShaderProgram();
		fogReady = 1;
		foginitLoc = glGetUniformLocation(myCustomShader.shaderProgram, "fogReady");
		glUniform1i(foginitLoc, fogReady);
	}

	//stop fog
	if (pressedKeys[GLFW_KEY_G]) {
		myCustomShader.useShaderProgram();
		fogReady = 0;
		foginitLoc = glGetUniformLocation(myCustomShader.shaderProgram, "fogReady");
		glUniform1i(foginitLoc, fogReady);
	}

	//increase the intensity of the fog if pressed + NM
	if (pressedKeys[GLFW_KEY_KP_ADD]) {
		myCustomShader.useShaderProgram();
		fogDensity += 0.01;
		fogDensityLoc = glGetUniformLocation(myCustomShader.shaderProgram, "fogIntensity");
		glUniform1f(fogDensityLoc, fogDensity);
	}

	//decrease the intensity of the fog if pressed - NM
	if (pressedKeys[GLFW_KEY_KP_SUBTRACT]) {
		myCustomShader.useShaderProgram();
		fogDensity -= 0.01;
		fogDensityLoc = glGetUniformLocation(myCustomShader.shaderProgram, "fogIntensity");
		glUniform1f(fogDensityLoc, fogDensity);
	}
	
	//night mode
	if (pressedKeys[GLFW_KEY_N]) {
		night = 1;
		myCustomShader.useShaderProgram();
		glUniform3fv(lightColorLoc, 1, glm::value_ptr(nightLightColor));
		glUniform1i(isNightLoc, night);
	}

	//night mode off
	if (pressedKeys[GLFW_KEY_B]) {
		night = 0;
		if (night == 0) {
			myCustomShader.useShaderProgram();
			glUniform3fv(lightColorLoc, 1, glm::value_ptr(lightColor));
		}

		glUniform1i(isNightLoc, night);
	}

	//rotate the turbine
	if (pressedKeys[GLFW_KEY_T]) {
		turbineRotation();
	}


	
	//inca ceva
}

bool initOpenGLWindow()
{
	if (!glfwInit()) {
		fprintf(stderr, "ERROR: could not start GLFW3\n");
		return false;
	}

	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
	glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);


	//window scaling for HiDPI displays
	glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);

	//for sRBG framebuffer
	glfwWindowHint(GLFW_SRGB_CAPABLE, GLFW_TRUE);

	//for antialising
	glfwWindowHint(GLFW_SAMPLES, 4);

	glWindow = glfwCreateWindow(glWindowWidth, glWindowHeight, "OpenGL Shader Example", NULL, NULL);
	if (!glWindow) {
		fprintf(stderr, "ERROR: could not open window with GLFW3\n");
		glfwTerminate();
		return false;
	}

	glfwSetWindowSizeCallback(glWindow, windowResizeCallback);
	glfwSetKeyCallback(glWindow, keyboardCallback);
	glfwSetCursorPosCallback(glWindow, mouseCallback);
	glfwSetInputMode(glWindow, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

	glfwMakeContextCurrent(glWindow);

	glfwSwapInterval(1);

#if not defined (__APPLE__)
	// start GLEW extension handler
	glewExperimental = GL_TRUE;
	glewInit();
#endif

	// get version info
	const GLubyte* renderer = glGetString(GL_RENDERER); // get renderer string
	const GLubyte* version = glGetString(GL_VERSION); // version as a string
	printf("Renderer: %s\n", renderer);
	printf("OpenGL version supported %s\n", version);

	//for RETINA display
	glfwGetFramebufferSize(glWindow, &retina_width, &retina_height);

	return true;
}

void initOpenGLState()
{
	glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
	glViewport(0, 0, retina_width, retina_height);

	glEnable(GL_DEPTH_TEST); // enable depth-testing
	glDepthFunc(GL_LESS); // depth-testing interprets a smaller value as "closer"
	glEnable(GL_CULL_FACE); // cull face
	glCullFace(GL_BACK); // cull back face
	glFrontFace(GL_CCW); // GL_CCW for counter clock-wise

	glEnable(GL_FRAMEBUFFER_SRGB);
}

void initObjects() {
	//AICI ADAUG OBIECTE
	/*
	nanosuit.LoadModel("objects/nanosuit/nanosuit.obj");
	ground.LoadModel("objects/ground/ground.obj");
	lightCube.LoadModel("objects/cube/cube.obj");
	screenQuad.LoadModel("objects/quad/quad.obj");
	*/
	myModel.LoadModel("models/scene_v1.obj"); //scena
	lightCube.LoadModel("objects/cube/cube.obj");
	carModel.LoadModel("models/car_anim.obj");
	turbineModel.LoadModel("models/wind_turbine_anim.obj");
}

void initShaders() {
	myCustomShader.loadShader("shaders/shaderStart.vert", "shaders/shaderStart.frag");
	myCustomShader.useShaderProgram();
	lightShader.loadShader("shaders/lightCube.vert", "shaders/lightCube.frag");
	lightShader.useShaderProgram();
	screenQuadShader.loadShader("shaders/screenQuad.vert", "shaders/screenQuad.frag");
	screenQuadShader.useShaderProgram();
	depthMapShader.loadShader("shaders/shadow.vert", "shaders/shadow.frag");
	depthMapShader.useShaderProgram();
	skyboxShader.loadShader("shaders/skyboxShader.vert", "shaders/skyboxShader.frag");
	skyboxShader.useShaderProgram();
}

void initUniforms() {
	myCustomShader.useShaderProgram();

	model = glm::mat4(1.0f);
	modelLoc = glGetUniformLocation(myCustomShader.shaderProgram, "model");
	glUniformMatrix4fv(modelLoc, 1, GL_FALSE, glm::value_ptr(model));

	view = myCamera.getViewMatrix();
	viewLoc = glGetUniformLocation(myCustomShader.shaderProgram, "view");
	glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));

	normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	normalMatrixLoc = glGetUniformLocation(myCustomShader.shaderProgram, "normalMatrix");
	glUniformMatrix3fv(normalMatrixLoc, 1, GL_FALSE, glm::value_ptr(normalMatrix));

	projection = glm::perspective(glm::radians(45.0f), (float)retina_width / (float)retina_height, 0.1f, 1000.0f);
	projectionLoc = glGetUniformLocation(myCustomShader.shaderProgram, "projection");
	glUniformMatrix4fv(projectionLoc, 1, GL_FALSE, glm::value_ptr(projection));

	//set the light direction (direction towards the light)
	//
	//lightDir = glm::vec3(0.0f, 1.0f, -1.0f);
	lightDir = glm::vec3(-20.0130f, 20.0f, 40.0f);
	lightRotation = glm::rotate(glm::mat4(1.0f), glm::radians(lightAngle), glm::vec3(0.0f, 1.0f, 0.0f));
	lightDirLoc = glGetUniformLocation(myCustomShader.shaderProgram, "lightDir");
	glUniform3fv(lightDirLoc, 1, glm::value_ptr(glm::inverseTranspose(glm::mat3(view * lightRotation)) * lightDir));

	//set light color
	lightColor = glm::vec3(1.0f, 1.0f, 1.0f); //white light
	lightColorLoc = glGetUniformLocation(myCustomShader.shaderProgram, "lightColor");
	glUniform3fv(lightColorLoc, 1, glm::value_ptr(lightColor));

	lightShader.useShaderProgram();
	glUniformMatrix4fv(glGetUniformLocation(lightShader.shaderProgram, "projection"), 1, GL_FALSE, glm::value_ptr(projection));
	
	//point light
	lightPos1 = glm::vec3(0.3f, 0.2f, 0.5f);
	lightPos1Loc = glGetUniformLocation(lightShader.shaderProgram, "lightPos1");
	glUniform3fv(lightPos1Loc, 1, glm::value_ptr(lightPos1));
	
	// For shadow mapping
	depthMapShader.useShaderProgram();
	glUniform1i(glGetUniformLocation(depthMapShader.shaderProgram, "shadowMap"), 0);
	
	//for night mode
	nightLightColorLoc = glGetUniformLocation(myCustomShader.shaderProgram, "nightColor");
	glUniform3fv(nightLightColorLoc, 1, glm::value_ptr(nightLightColor));
	
	isNightLoc = glGetUniformLocation(myCustomShader.shaderProgram, "isNight");
	glUniform1i(isNightLoc, night);

}

void initFBO() {
	//TODO - Create the FBO, the depth texture and attach the depth texture to the FBO
	//FBO ID
	glGenFramebuffers(1, &shadowMapFBO);

	//create deep texture for FBO
	glGenTextures(1, &depthMapTexture);
	glBindTexture(GL_TEXTURE_2D, depthMapTexture);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, SHADOW_WIDTH, SHADOW_HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	float borderColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor); //clampColor
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

	//attach the texture to the FBO
	glBindFramebuffer(GL_FRAMEBUFFER, shadowMapFBO);
	glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMapTexture, 0);

	glDrawBuffer(GL_NONE);
	glReadBuffer(GL_NONE);

	glBindFramebuffer(GL_FRAMEBUFFER, 0);
}

glm::mat4 computeLightSpaceTrMatrix() {
	//TODO - Return the light-space transformation matrix
	//
	glm::vec3 lightPosition = glm::vec3(-4.01307f, 10.0f, 35.0f);
	lightRotation = glm::rotate(glm::mat4(1.0f), glm::radians(lightAngle), glm::vec3(0.0f, 1.0f, 0.0f));
	glm::mat4 lightView = glm::lookAt(glm::vec3(lightRotation * glm::vec4(lightDir, 1.0f)), glm::vec3(0.0f), glm::vec3(0.0f, 1.0f, 0.0f));
	const GLfloat nearPlane = -8.0f, farPlane = 8.0f; //250
	glm::mat4 lightProjection = glm::ortho(-8.0f, 8.0f, -8.0f, 8.0f, nearPlane, farPlane);
	glm::mat4 lightSpaceTrMatrix = lightProjection * lightView;

	return lightSpaceTrMatrix;
}

//skybox ul
void  initSkybox() {
	std::vector<const GLchar*> faces;
	faces.push_back("skybox/right.png");
	faces.push_back("skybox/left.png");
	faces.push_back("skybox/top.png");
	faces.push_back("skybox/bottom.png");
	faces.push_back("skybox/back.png");
	faces.push_back("skybox/front.png");
	mySkyBox.Load(faces);
}
float counter = 0.0f;
float step = 0.05f;
int on = 0;
glm::vec3 ref = glm::vec3(39.883f, 12.099f, 37.695f);
void drawObjects(gps::Shader shader, bool depthPass) {
	//39.833 37.695 12.099
	shader.useShaderProgram();

	//turbine animation
	glm::mat4 object;
	//glm::vec3 ref = glm::vec3(40.0f, 40.0f, 12.0f);  //(-0.01931f, -0.06145f, -0.04221f);
	object = glm::mat4(1.0f);
	object = glm::translate(object, ref);
	object = glm::rotate(object, glm::radians(alpha), glm::vec3(0.0f, 0.0f, 1.0f));
	object = glm::translate(object, -ref);

	glUniformMatrix4fv(modelLoc, 1, GL_FALSE, glm::value_ptr(object));
	glUniformMatrix3fv(normalMatrixLoc, 1, GL_FALSE, glm::value_ptr(normalMatrix));
	//glUniformMatrix3fv(normalMatrixLoc, 1, GL_FALSE, glm::value_ptr(glm::mat3(glm::transpose(glm::inverse(view * object)))));
	turbineModel.Draw(shader);
	
	//car animation
	glm::mat4 carMatrix(1.0f);
	carMatrix = glm::translate(carMatrix, glm::vec3(0, 0, counter));
	glUniformMatrix4fv(modelLoc, 1, GL_FALSE, glm::value_ptr(carMatrix));
	glm::mat3 carNormalMatrix = glm::mat3(glm::inverseTranspose(view * carMatrix));
	glUniformMatrix3fv(normalMatrixLoc, 1, GL_FALSE, glm::value_ptr(carNormalMatrix));
	counter += step;
	if (counter > 60.5f) {
		step = step * (-1);
	}
	if (counter < 0.0f) {
		step = step * (-1);
	}
	carModel.Draw(shader);

	model = glm::rotate(glm::mat4(1.0f), glm::radians(angleY), glm::vec3(0.0f, 1.0f, 0.0f));
	glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(model));

	// do not send the normal matrix if we are rendering in the depth map
	if (!depthPass) {
		normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
		glUniformMatrix3fv(normalMatrixLoc, 1, GL_FALSE, glm::value_ptr(normalMatrix));
	}

	//nanosuit.Draw(shader);

	model = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, -1.0f, 0.0f));
	model = glm::scale(model, glm::vec3(0.5f));
	glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(model));

	//AICI PUN OBIECTE
	//scene_v1.Draw(shader);
	myModel.Draw(shader);
	mySkyBox.Draw(skyboxShader, view, projection);
	
}

				
void renderScene() {

	// depth maps creation pass
	//TODO - Send the light-space transformation matrix to the depth map creation shader and
	//		 render the scene in the depth map

	
	depthMapShader.useShaderProgram();
	glUniformMatrix4fv(glGetUniformLocation(depthMapShader.shaderProgram, "lightSpaceTrMatrix"), 1, GL_FALSE, glm::value_ptr(computeLightSpaceTrMatrix()));
	glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
	glBindFramebuffer(GL_FRAMEBUFFER, shadowMapFBO);
	glClear(GL_DEPTH_BUFFER_BIT);
	//
	depthMapShader.useShaderProgram();
	model = glm::mat4(1.0f);
	drawObjects(depthMapShader, 1);
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
	//

	// render depth map on screen - toggled with the M key

	if (showDepthMap) {
		glViewport(0, 0, retina_width, retina_height);

		glClear(GL_COLOR_BUFFER_BIT);

		screenQuadShader.useShaderProgram();

		//bind the depth map
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, depthMapTexture);
		glUniform1i(glGetUniformLocation(screenQuadShader.shaderProgram, "depthMap"), 0);

		glDisable(GL_DEPTH_TEST);
		screenQuad.Draw(screenQuadShader);
		glEnable(GL_DEPTH_TEST);
	}
	else {

		// final scene rendering pass (with shadows)

		glViewport(0, 0, retina_width, retina_height);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		myCustomShader.useShaderProgram();

		view = myCamera.getViewMatrix();
		glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));

		lightRotation = glm::rotate(glm::mat4(1.0f), glm::radians(lightAngle), glm::vec3(0.0f, 1.0f, 0.0f));
		glUniform3fv(lightDirLoc, 1, glm::value_ptr(glm::inverseTranspose(glm::mat3(view * lightRotation)) * lightDir));

		//bind the shadow map
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, depthMapTexture);
		glUniform1i(glGetUniformLocation(myCustomShader.shaderProgram, "shadowMap"), 3);

		glUniformMatrix4fv(glGetUniformLocation(myCustomShader.shaderProgram, "lightSpaceTrMatrix"), 1, GL_FALSE, glm::value_ptr(computeLightSpaceTrMatrix()));

		drawObjects(myCustomShader, false);

		//draw a white cube around the light

		//
		lightShader.useShaderProgram();

		glUniformMatrix4fv(glGetUniformLocation(lightShader.shaderProgram, "view"), 1, GL_FALSE, glm::value_ptr(view));

		model = lightRotation;
		model = glm::translate(model, 1.0f * lightDir);
		model = glm::scale(model, glm::vec3(0.05f, 0.05f, 0.05f));
		glUniformMatrix4fv(glGetUniformLocation(lightShader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(model));

		lightCube.Draw(lightShader);
		
	}
}

void cleanup() {
	glDeleteTextures(1, &depthMapTexture);
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
	glDeleteFramebuffers(1, &shadowMapFBO);
	glfwDestroyWindow(glWindow);
	//close GL context and any other GLFW resources
	glfwTerminate();
}

int main(int argc, const char* argv[]) {

	if (!initOpenGLWindow()) {
		glfwTerminate();
		return 1;
	}

	initOpenGLState();
	initObjects();
	initShaders();
	initSkybox();
	initUniforms();
	initFBO();

	glCheckError();

	while (!glfwWindowShouldClose(glWindow)) {
		processMovement();
		renderScene();


		glfwPollEvents();
		glfwSwapBuffers(glWindow);
	}

	cleanup();

	return 0;
}
