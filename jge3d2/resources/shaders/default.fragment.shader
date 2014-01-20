#version 330
        
uniform mat4 transform;

uniform TransformationMatrices { 
        mat4 MVP;
};

in vec3 vertex;
in vec3 normal;
in vec2 texture;
//in vec4 color;
uniform vec4 scale;
smooth out vec3 vertex_mod;
smooth out vec3 normal_mod;
out vec2 tex_coord;

void main() {
        vec4 vertex_cast;
        vertex_cast.x = vertex.x * scale.x;
        vertex_cast.y = vertex.y * scale.y;
        vertex_cast.z = vertex.z * scale.z;
        vertex_cast.w = 1.0 * scale.w;
        
        vertex_cast = transform * vertex_cast;

        vertex_mod = vec3(vertex_cast);

        //Calculate vertex position
        gl_Position = MVP * vertex_cast;

        // Calculate the normal value for this vertex, in world coordinates
    normal_mod = vec3(transform * vec4(normal.x,normal.y,normal.z,1.0));

        tex_coord = texture;

    // Set the front color to the color passed through with glColor
        //color_mod = color;
}