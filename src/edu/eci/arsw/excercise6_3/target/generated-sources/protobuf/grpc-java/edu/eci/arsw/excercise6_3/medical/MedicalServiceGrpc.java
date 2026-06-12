package edu.eci.arsw.excercise6_3.medical;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: medical.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MedicalServiceGrpc {

  private MedicalServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "MedicalService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.medical.SpecialtyRequest,
      edu.eci.arsw.excercise6_3.medical.SpecialtyResponse> getGetSpecialtyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSpecialty",
      requestType = edu.eci.arsw.excercise6_3.medical.SpecialtyRequest.class,
      responseType = edu.eci.arsw.excercise6_3.medical.SpecialtyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.medical.SpecialtyRequest,
      edu.eci.arsw.excercise6_3.medical.SpecialtyResponse> getGetSpecialtyMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.medical.SpecialtyRequest, edu.eci.arsw.excercise6_3.medical.SpecialtyResponse> getGetSpecialtyMethod;
    if ((getGetSpecialtyMethod = MedicalServiceGrpc.getGetSpecialtyMethod) == null) {
      synchronized (MedicalServiceGrpc.class) {
        if ((getGetSpecialtyMethod = MedicalServiceGrpc.getGetSpecialtyMethod) == null) {
          MedicalServiceGrpc.getGetSpecialtyMethod = getGetSpecialtyMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise6_3.medical.SpecialtyRequest, edu.eci.arsw.excercise6_3.medical.SpecialtyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetSpecialty"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.medical.SpecialtyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.medical.SpecialtyResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MedicalServiceMethodDescriptorSupplier("GetSpecialty"))
              .build();
        }
      }
    }
    return getGetSpecialtyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.medical.MedicalEmpty,
      edu.eci.arsw.excercise6_3.medical.SpecialtyList> getListSpecialtiesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListSpecialties",
      requestType = edu.eci.arsw.excercise6_3.medical.MedicalEmpty.class,
      responseType = edu.eci.arsw.excercise6_3.medical.SpecialtyList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.medical.MedicalEmpty,
      edu.eci.arsw.excercise6_3.medical.SpecialtyList> getListSpecialtiesMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.medical.MedicalEmpty, edu.eci.arsw.excercise6_3.medical.SpecialtyList> getListSpecialtiesMethod;
    if ((getListSpecialtiesMethod = MedicalServiceGrpc.getListSpecialtiesMethod) == null) {
      synchronized (MedicalServiceGrpc.class) {
        if ((getListSpecialtiesMethod = MedicalServiceGrpc.getListSpecialtiesMethod) == null) {
          MedicalServiceGrpc.getListSpecialtiesMethod = getListSpecialtiesMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise6_3.medical.MedicalEmpty, edu.eci.arsw.excercise6_3.medical.SpecialtyList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListSpecialties"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.medical.MedicalEmpty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.medical.SpecialtyList.getDefaultInstance()))
              .setSchemaDescriptor(new MedicalServiceMethodDescriptorSupplier("ListSpecialties"))
              .build();
        }
      }
    }
    return getListSpecialtiesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.medical.AddSpecialtyRequest,
      edu.eci.arsw.excercise6_3.medical.AddSpecialtyResponse> getAddSpecialtyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddSpecialty",
      requestType = edu.eci.arsw.excercise6_3.medical.AddSpecialtyRequest.class,
      responseType = edu.eci.arsw.excercise6_3.medical.AddSpecialtyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.medical.AddSpecialtyRequest,
      edu.eci.arsw.excercise6_3.medical.AddSpecialtyResponse> getAddSpecialtyMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.medical.AddSpecialtyRequest, edu.eci.arsw.excercise6_3.medical.AddSpecialtyResponse> getAddSpecialtyMethod;
    if ((getAddSpecialtyMethod = MedicalServiceGrpc.getAddSpecialtyMethod) == null) {
      synchronized (MedicalServiceGrpc.class) {
        if ((getAddSpecialtyMethod = MedicalServiceGrpc.getAddSpecialtyMethod) == null) {
          MedicalServiceGrpc.getAddSpecialtyMethod = getAddSpecialtyMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise6_3.medical.AddSpecialtyRequest, edu.eci.arsw.excercise6_3.medical.AddSpecialtyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddSpecialty"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.medical.AddSpecialtyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.medical.AddSpecialtyResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MedicalServiceMethodDescriptorSupplier("AddSpecialty"))
              .build();
        }
      }
    }
    return getAddSpecialtyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyRequest,
      edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyResponse> getRemoveSpecialtyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RemoveSpecialty",
      requestType = edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyRequest.class,
      responseType = edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyRequest,
      edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyResponse> getRemoveSpecialtyMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyRequest, edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyResponse> getRemoveSpecialtyMethod;
    if ((getRemoveSpecialtyMethod = MedicalServiceGrpc.getRemoveSpecialtyMethod) == null) {
      synchronized (MedicalServiceGrpc.class) {
        if ((getRemoveSpecialtyMethod = MedicalServiceGrpc.getRemoveSpecialtyMethod) == null) {
          MedicalServiceGrpc.getRemoveSpecialtyMethod = getRemoveSpecialtyMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyRequest, edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RemoveSpecialty"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MedicalServiceMethodDescriptorSupplier("RemoveSpecialty"))
              .build();
        }
      }
    }
    return getRemoveSpecialtyMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MedicalServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MedicalServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MedicalServiceStub>() {
        @java.lang.Override
        public MedicalServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MedicalServiceStub(channel, callOptions);
        }
      };
    return MedicalServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MedicalServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MedicalServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MedicalServiceBlockingStub>() {
        @java.lang.Override
        public MedicalServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MedicalServiceBlockingStub(channel, callOptions);
        }
      };
    return MedicalServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MedicalServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MedicalServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MedicalServiceFutureStub>() {
        @java.lang.Override
        public MedicalServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MedicalServiceFutureStub(channel, callOptions);
        }
      };
    return MedicalServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getSpecialty(edu.eci.arsw.excercise6_3.medical.SpecialtyRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.medical.SpecialtyResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetSpecialtyMethod(), responseObserver);
    }

    /**
     */
    default void listSpecialties(edu.eci.arsw.excercise6_3.medical.MedicalEmpty request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.medical.SpecialtyList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListSpecialtiesMethod(), responseObserver);
    }

    /**
     */
    default void addSpecialty(edu.eci.arsw.excercise6_3.medical.AddSpecialtyRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.medical.AddSpecialtyResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddSpecialtyMethod(), responseObserver);
    }

    /**
     */
    default void removeSpecialty(edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRemoveSpecialtyMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service MedicalService.
   */
  public static abstract class MedicalServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MedicalServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service MedicalService.
   */
  public static final class MedicalServiceStub
      extends io.grpc.stub.AbstractAsyncStub<MedicalServiceStub> {
    private MedicalServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MedicalServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MedicalServiceStub(channel, callOptions);
    }

    /**
     */
    public void getSpecialty(edu.eci.arsw.excercise6_3.medical.SpecialtyRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.medical.SpecialtyResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetSpecialtyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listSpecialties(edu.eci.arsw.excercise6_3.medical.MedicalEmpty request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.medical.SpecialtyList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListSpecialtiesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addSpecialty(edu.eci.arsw.excercise6_3.medical.AddSpecialtyRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.medical.AddSpecialtyResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddSpecialtyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeSpecialty(edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRemoveSpecialtyMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service MedicalService.
   */
  public static final class MedicalServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MedicalServiceBlockingStub> {
    private MedicalServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MedicalServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MedicalServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public edu.eci.arsw.excercise6_3.medical.SpecialtyResponse getSpecialty(edu.eci.arsw.excercise6_3.medical.SpecialtyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSpecialtyMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.excercise6_3.medical.SpecialtyList listSpecialties(edu.eci.arsw.excercise6_3.medical.MedicalEmpty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListSpecialtiesMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.excercise6_3.medical.AddSpecialtyResponse addSpecialty(edu.eci.arsw.excercise6_3.medical.AddSpecialtyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddSpecialtyMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyResponse removeSpecialty(edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRemoveSpecialtyMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service MedicalService.
   */
  public static final class MedicalServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<MedicalServiceFutureStub> {
    private MedicalServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MedicalServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MedicalServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise6_3.medical.SpecialtyResponse> getSpecialty(
        edu.eci.arsw.excercise6_3.medical.SpecialtyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetSpecialtyMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise6_3.medical.SpecialtyList> listSpecialties(
        edu.eci.arsw.excercise6_3.medical.MedicalEmpty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListSpecialtiesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise6_3.medical.AddSpecialtyResponse> addSpecialty(
        edu.eci.arsw.excercise6_3.medical.AddSpecialtyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddSpecialtyMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyResponse> removeSpecialty(
        edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRemoveSpecialtyMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_SPECIALTY = 0;
  private static final int METHODID_LIST_SPECIALTIES = 1;
  private static final int METHODID_ADD_SPECIALTY = 2;
  private static final int METHODID_REMOVE_SPECIALTY = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_SPECIALTY:
          serviceImpl.getSpecialty((edu.eci.arsw.excercise6_3.medical.SpecialtyRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.medical.SpecialtyResponse>) responseObserver);
          break;
        case METHODID_LIST_SPECIALTIES:
          serviceImpl.listSpecialties((edu.eci.arsw.excercise6_3.medical.MedicalEmpty) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.medical.SpecialtyList>) responseObserver);
          break;
        case METHODID_ADD_SPECIALTY:
          serviceImpl.addSpecialty((edu.eci.arsw.excercise6_3.medical.AddSpecialtyRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.medical.AddSpecialtyResponse>) responseObserver);
          break;
        case METHODID_REMOVE_SPECIALTY:
          serviceImpl.removeSpecialty((edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetSpecialtyMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise6_3.medical.SpecialtyRequest,
              edu.eci.arsw.excercise6_3.medical.SpecialtyResponse>(
                service, METHODID_GET_SPECIALTY)))
        .addMethod(
          getListSpecialtiesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise6_3.medical.MedicalEmpty,
              edu.eci.arsw.excercise6_3.medical.SpecialtyList>(
                service, METHODID_LIST_SPECIALTIES)))
        .addMethod(
          getAddSpecialtyMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise6_3.medical.AddSpecialtyRequest,
              edu.eci.arsw.excercise6_3.medical.AddSpecialtyResponse>(
                service, METHODID_ADD_SPECIALTY)))
        .addMethod(
          getRemoveSpecialtyMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyRequest,
              edu.eci.arsw.excercise6_3.medical.RemoveSpecialtyResponse>(
                service, METHODID_REMOVE_SPECIALTY)))
        .build();
  }

  private static abstract class MedicalServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MedicalServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.eci.arsw.excercise6_3.medical.MedicalProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MedicalService");
    }
  }

  private static final class MedicalServiceFileDescriptorSupplier
      extends MedicalServiceBaseDescriptorSupplier {
    MedicalServiceFileDescriptorSupplier() {}
  }

  private static final class MedicalServiceMethodDescriptorSupplier
      extends MedicalServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    MedicalServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MedicalServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MedicalServiceFileDescriptorSupplier())
              .addMethod(getGetSpecialtyMethod())
              .addMethod(getListSpecialtiesMethod())
              .addMethod(getAddSpecialtyMethod())
              .addMethod(getRemoveSpecialtyMethod())
              .build();
        }
      }
    }
    return result;
  }
}
